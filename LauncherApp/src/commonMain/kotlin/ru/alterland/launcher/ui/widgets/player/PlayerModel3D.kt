package ru.alterland.launcher.ui.widgets.player

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import ru.alterland.launcher.domain.model.Skin
import ru.alterland.launcher.ui.widgets.player.elements.Box3D
import ru.alterland.launcher.ui.widgets.player.elements.Face
import ru.alterland.launcher.ui.widgets.player.elements.Vertex3D

private const val MODEL_CENTER_Y = 16f
private const val MODEL_SCALE_FACTOR = 28f

@Composable
fun PlayerModel3D(
    modifier: Modifier = Modifier,
    skin: Skin,
    baseColor: Color = Color(0xFF808080),
    rotationSpeed: Int = 10000
) {
//    val rotationY by rememberInfiniteTransition().animateFloat(
//        initialValue = 0f,
//        targetValue = 360f,
//        animationSpec = infiniteRepeatable(
//            animation = tween(durationMillis = rotationSpeed, easing = LinearEasing),
//            repeatMode = RepeatMode.Restart
//        )
//    )
//
//    val boxes = remember(skin) {
//        if (skin.modelType == Skin.ModelType.WIDE) wideModel else slimModel
//    }
//
//    val model = remember(skin, baseColor) { buildModel(boxes, baseColor) }
//    val skiaImage = remember(texture) { texture?.let { Image.makeFromBitmap(it.asSkiaBitmap()) } }
//    val paint = remember { SkiaPaint().apply { isAntiAlias = false } }
//    val reusablePath = remember { Path() }
//
//    Canvas(modifier = modifier) {
//        val centerX = size.width / 2
//        val centerY = size.height / 2
//        val scale = size.minDimension / MODEL_SCALE_FACTOR
//
//        val radY = Math.toRadians(rotationY.toDouble())
//        val cosY = cos(radY).toFloat()
//        val sinY = sin(radY).toFloat()
//
//        val projected = model.projectedPositions
//        val depths = model.depths
//
//        model.vertices.forEachIndexed { i, vertex ->
//            val y = vertex.y - MODEL_CENTER_Y
//            val rotatedX = vertex.x * cosY + vertex.z * sinY
//            projected[i] = Offset(centerX + rotatedX * scale, centerY - y * scale)
//            depths[i] = -vertex.x * sinY + vertex.z * cosY
//        }
//
//        model.faces.forEachIndexed { i, face ->
//            var sum = 0.0
//            for (v in face.vertices) sum += depths[v]
//            model.faceDepths[i] = sum / face.vertices.size
//        }
//
//        val sortedFaces = model.faces.indices.sortedBy { model.faceDepths[it] }
//
//        for (idx in sortedFaces) {
//            val face = model.faces[idx]
//            val p0 = projected[face.vertices[0]]
//            val p1 = projected[face.vertices[1]]
//            val p2 = projected[face.vertices[2]]
//            val p3 = projected[face.vertices[3]]
//
//            reusablePath.reset()
//            reusablePath.moveTo(p0.x, p0.y)
//            reusablePath.lineTo(p1.x, p1.y)
//            reusablePath.lineTo(p2.x, p2.y)
//            reusablePath.lineTo(p3.x, p3.y)
//            reusablePath.close()
//
//            if (skiaImage != null && face.uvRect != null) {
//                val minX = minOf(p0.x, p1.x, p2.x, p3.x)
//                val maxX = maxOf(p0.x, p1.x, p2.x, p3.x)
//                val minY = minOf(p0.y, p1.y, p2.y, p3.y)
//                val maxY = maxOf(p0.y, p1.y, p2.y, p3.y)
//
//                clipPath(reusablePath) {
//                    drawIntoCanvas { canvas ->
//                        canvas.nativeCanvas.drawImageRect(
//                            skiaImage,
//                            SkiaRect.makeLTRB(face.uvRect.left, face.uvRect.top, face.uvRect.right, face.uvRect.bottom),
//                            SkiaRect.makeLTRB(minX, minY, maxX, maxY),
//                            paint
//                        )
//                    }
//                }
//            } else {
//                drawPath(reusablePath, face.color)
//            }
//        }
//    }
}

private class Model(
    val vertices: List<Vertex3D>,
    val faces: List<Face>,
    val projectedPositions: Array<Offset>,
    val depths: FloatArray,
    val faceDepths: DoubleArray
)

private fun buildModel(boxes: List<Box3D>, color: Color): Model {
    val vertices = mutableListOf<Vertex3D>()
    val faces = mutableListOf<Face>()

    boxes.forEach { box ->
        val base = vertices.size
        vertices.addAll(box.createVertices())
        faces.addAll(box.createFaces(base, color))
    }

    return Model(
        vertices = vertices,
        faces = faces,
        projectedPositions = Array(vertices.size) { Offset.Zero },
        depths = FloatArray(vertices.size),
        faceDepths = DoubleArray(faces.size)
    )
}

private fun Box3D.createVertices(): List<Vertex3D> {
    val (x, y, z) = Triple(originX, originY, originZ)
    val (sx, sy, sz) = Triple(sizeX, sizeY, sizeZ)

    return listOf(
        Vertex3D(x, y, z),
        Vertex3D(x + sx, y, z),
        Vertex3D(x + sx, y, z + sz),
        Vertex3D(x, y, z + sz),
        Vertex3D(x, y + sy, z),
        Vertex3D(x + sx, y + sy, z),
        Vertex3D(x + sx, y + sy, z + sz),
        Vertex3D(x, y + sy, z + sz)
    )
}

private fun Box3D.createFaces(base: Int, color: Color): List<Face> {
    val w = uvW ?: sizeX.toInt()
    val h = uvH ?: sizeY.toInt()
    val d = uvD ?: sizeZ.toInt()
    val u = uvX
    val v = uvY

    fun uv(x: Int, y: Int, w: Int, h: Int) = Rect(x.toFloat(), y.toFloat(), (x + w).toFloat(), (y + h).toFloat())

    return listOf(
        Face(intArrayOf(base + 3, base + 2, base + 6, base + 7), color, uv(u + d, v + d, w, h)),
        Face(intArrayOf(base + 1, base + 0, base + 4, base + 5), color, uv(u + d + w + d, v + d, w, h)),
        Face(intArrayOf(base + 0, base + 3, base + 7, base + 4), color, uv(u, v + d, d, h)),
        Face(intArrayOf(base + 2, base + 1, base + 5, base + 6), color, uv(u + d + w, v + d, d, h)),
        Face(intArrayOf(base + 4, base + 7, base + 6, base + 5), color, uv(u + d, v, w, d)),
        Face(intArrayOf(base + 0, base + 1, base + 2, base + 3), color, uv(u + d + w, v, w, d))
    )
}
