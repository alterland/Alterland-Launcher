package ru.alterland.launcher.ui.widgets

import org.lwjgl.system.MemoryStack
import org.lwjgl.vulkan.KHRSurface
import org.lwjgl.vulkan.KHRSurface.VK_KHR_SURFACE_EXTENSION_NAME
import org.lwjgl.vulkan.VK10.VK_MAKE_VERSION
import org.lwjgl.vulkan.VK10.VK_STRUCTURE_TYPE_APPLICATION_INFO
import org.lwjgl.vulkan.VK10.VK_STRUCTURE_TYPE_INSTANCE_CREATE_INFO
import org.lwjgl.vulkan.VK10.VK_SUCCESS
import org.lwjgl.vulkan.VK10.vkCreateInstance
import org.lwjgl.vulkan.VkApplicationInfo
import org.lwjgl.vulkan.VkInstance
import org.lwjgl.vulkan.VkInstanceCreateInfo
import org.lwjgl.vulkan.awt.AWTVK
import java.awt.Canvas
import java.awt.Color
import java.awt.Graphics2D
import java.awt.RenderingHints
import java.awt.Toolkit
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin
import kotlin.use

class LWJGLCanvas(
    private val backgroundColor: Color = Color(0,0,0)
) : Canvas() {

    private var instance: VkInstance? = null
    private var surface: Long = 0
    private var lastFrameTime = System.nanoTime()
    private var angle = 0.0

    private fun create() {
        instance = createVkInstance()
        surface = AWTVK.create(this, instance)
    }

    fun paint() {
        if (surface == 0L) {
            create()
        }
        updateAngle()
        val strategy = bufferStrategy ?: run {
            createBufferStrategy(2)
            bufferStrategy
        }
        val graphics = strategy?.drawGraphics as? Graphics2D ?: return

        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
        graphics.color = backgroundColor
        graphics.fillRect(0, 0, width, height)

        drawCube(graphics, angle)

        graphics.dispose()
        strategy.show()
        Toolkit.getDefaultToolkit().sync()
    }

    fun destroy() {
        instance?.let {
            KHRSurface.vkDestroySurfaceKHR(it, surface, null)
            instance = null
        }
    }

    private fun updateAngle() {
        val now = System.nanoTime()
        val delta = (now - lastFrameTime) / 1_000_000_000.0
        lastFrameTime = now
        angle += delta * 0.8
    }

    private fun drawCube(graphics: Graphics2D, rotation: Double) {
        val cubeSize = min(width, height) * 0.5
        val vertices = listOf(
            Vec3(-1.0, -1.0, -1.0),
            Vec3(1.0, -1.0, -1.0),
            Vec3(1.0, 1.0, -1.0),
            Vec3(-1.0, 1.0, -1.0),
            Vec3(-1.0, -1.0, 1.0),
            Vec3(1.0, -1.0, 1.0),
            Vec3(1.0, 1.0, 1.0),
            Vec3(-1.0, 1.0, 1.0)
        )

        val angleX = rotation * 0.7
        val angleY = rotation

        val projected = vertices.map { vertex ->
            val rotated = rotate(vertex, angleX, angleY)
            project(rotated, cubeSize)
        }

        val edges = listOf(
            0 to 1, 1 to 2, 2 to 3, 3 to 0,
            4 to 5, 5 to 6, 6 to 7, 7 to 4,
            0 to 4, 1 to 5, 2 to 6, 3 to 7
        )

        graphics.color = Color(0x2C, 0xA5, 0xFF)
        edges.forEach { (start, end) ->
            val from = projected[start]
            val to = projected[end]
            graphics.drawLine(from.first, from.second, to.first, to.second)
        }

        // Add a subtle glow by redrawing the edges with fading alpha.
        graphics.color = Color(0x2C, 0xA5, 0xFF, 80)
        edges.forEach { (start, end) ->
            val from = projected[start]
            val to = projected[end]
            graphics.drawLine(from.first, from.second, to.first, to.second)
        }
    }

    private fun rotate(vertex: Vec3, angleX: Double, angleY: Double): Vec3 {
        val sinX = sin(angleX)
        val cosX = cos(angleX)
        val sinY = sin(angleY)
        val cosY = cos(angleY)

        val y = vertex.y * cosX - vertex.z * sinX
        val z = vertex.y * sinX + vertex.z * cosX

        val x2 = vertex.x * cosY + z * sinY
        val z2 = -vertex.x * sinY + z * cosY

        return Vec3(x2, y, z2)
    }

    private fun project(vertex: Vec3, scale: Double): Pair<Int, Int> {
        val distance = 4.0
        val z = vertex.z + distance
        val factor = scale / z
        val x = (vertex.x * factor) + width / 2.0
        val y = (-vertex.y * factor) + height / 2.0
        return Pair(x.toInt(), y.toInt())
    }

    private data class Vec3(val x: Double, val y: Double, val z: Double)

    companion object Companion {
        private fun createVkInstance(): VkInstance {
            MemoryStack.stackPush().use { stack ->
                val appInfo = VkApplicationInfo
                    .calloc(stack)
                    .sType(VK_STRUCTURE_TYPE_APPLICATION_INFO)
                    .pApplicationName(stack.UTF8("AWT Vulkan Renderer"))
                    .pEngineName(stack.UTF8(""))
                    .apiVersion(VK_MAKE_VERSION(1, 0, 2))

                val ppEnabledExtensionNames = stack.pointers(
                    stack.UTF8(VK_KHR_SURFACE_EXTENSION_NAME),
                    stack.UTF8(AWTVK.getSurfaceExtensionName())
                )

                val pCreateInfo = VkInstanceCreateInfo
                    .calloc(stack)
                    .sType(VK_STRUCTURE_TYPE_INSTANCE_CREATE_INFO)
                    .pApplicationInfo(appInfo)
                    .ppEnabledExtensionNames(ppEnabledExtensionNames)

                val pointerBuffer = stack.mallocPointer(1)
                val err: Int = vkCreateInstance(pCreateInfo, null, pointerBuffer)
                if (err != VK_SUCCESS) {
                    throw RuntimeException("Failed to create VkInstance: $err")
                }

                val instance = pointerBuffer.get(0)
                return VkInstance(instance, pCreateInfo)
            }
        }
    }
}
