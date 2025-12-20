package ru.alterland.launcher.ui.widgets.skinview.model

import kotlin.math.cos
import kotlin.math.sin

data class Vertex3D(
    val x: Float,
    val y: Float,
    val z: Float
) {
    fun rotateX(angle: Float, pivotY: Float = 0f, pivotZ: Float = 0f): Vertex3D {
        val cosA = cos(angle)
        val sinA = sin(angle)
        val newY = (y - pivotY) * cosA - (z - pivotZ) * sinA + pivotY
        val newZ = (y - pivotY) * sinA + (z - pivotZ) * cosA + pivotZ
        return Vertex3D(x, newY, newZ)
    }

    fun rotateY(angle: Float, pivotX: Float = 0f, pivotZ: Float = 0f): Vertex3D {
        val cosA = cos(angle)
        val sinA = sin(angle)
        val newX = (x - pivotX) * cosA + (z - pivotZ) * sinA + pivotX
        val newZ = -(x - pivotX) * sinA + (z - pivotZ) * cosA + pivotZ
        return Vertex3D(newX, y, newZ)
    }

    fun rotateZ(angle: Float, pivotX: Float = 0f, pivotY: Float = 0f): Vertex3D {
        val cosA = cos(angle)
        val sinA = sin(angle)
        val newX = (x - pivotX) * cosA - (y - pivotY) * sinA + pivotX
        val newY = (x - pivotX) * sinA + (y - pivotY) * cosA + pivotY
        return Vertex3D(newX, newY, z)
    }

    fun translate(dx: Float, dy: Float, dz: Float): Vertex3D {
        return Vertex3D(x + dx, y + dy, z + dz)
    }

    operator fun plus(other: Vertex3D) = Vertex3D(x + other.x, y + other.y, z + other.z)
    operator fun minus(other: Vertex3D) = Vertex3D(x - other.x, y - other.y, z - other.z)
    operator fun times(scalar: Float) = Vertex3D(x * scalar, y * scalar, z * scalar)

    fun cross(other: Vertex3D): Vertex3D {
        return Vertex3D(
            y * other.z - z * other.y,
            z * other.x - x * other.z,
            x * other.y - y * other.x
        )
    }

    fun normalize(): Vertex3D {
        val len = kotlin.math.sqrt(x * x + y * y + z * z)
        return if (len > 0) Vertex3D(x / len, y / len, z / len) else this
    }
}
