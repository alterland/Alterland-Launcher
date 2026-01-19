package ru.alterland.launcher.ui.widgets.skinview

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameMillis
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PixelMap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.toPixelMap
import androidx.compose.ui.input.pointer.pointerInput
import ru.alterland.launcher.ui.widgets.skinview.animation.getAnimation
import ru.alterland.launcher.ui.widgets.skinview.model.Face
import ru.alterland.launcher.ui.widgets.skinview.model.PlayerModel
import ru.alterland.launcher.ui.widgets.skinview.model.ProjectedFace
import kotlin.math.roundToInt

private const val TEXTURE_SIZE = 64
private const val OUTER_LAYER_Z_OFFSET = 0.001f
private const val TEXEL_OVERLAP = 0.01f

@Composable
fun SkinView(
    modifier: Modifier = Modifier,
    state: SkinViewState,
    enableDragRotation: Boolean = true
) {
    val playerModel = remember(state.modelType) {
        PlayerModel(state.modelType)
    }

    val animation = remember(state.animationType) {
        getAnimation(state.animationType)
    }

    var animationProgress by remember { mutableFloatStateOf(0f) }

    val skinBitmap = state.skinBitmap
    val pixelMap = remember(skinBitmap) {
        skinBitmap?.toPixelMap()
    }

    // Animation loop
    LaunchedEffect(state.isPaused, state.animationType) {
        if (!state.isPaused) {
            var lastFrameTime = withFrameMillis { it }
            while (true) {
                val frameTime = withFrameMillis { it }
                val deltaTime = (frameTime - lastFrameTime) / 1000f
                lastFrameTime = frameTime
                animationProgress += deltaTime * state.animationSpeed
            }
        }
    }

    val dragModifier = if (enableDragRotation) {
        Modifier.pointerInput(Unit) {
            detectDragGestures { change, dragAmount ->
                change.consume()
                state.rotate(dragAmount.x, dragAmount.y)
            }
        }
    } else Modifier

    Canvas(modifier = modifier.then(dragModifier)) {
        val centerX = size.width / 2
        val centerY = size.height / 2

        // Model height is 32 pixels (-16 to +16), width ~16 pixels
        // Calculate scale to fit model in widget with padding
        val scale = minOf(size.width / 20f, size.height / 36f)

        val animationState = animation.animate(animationProgress)

        val faces = playerModel.getAllFaces(
            state = animationState,
            globalRotationX = state.rotationX,
            globalRotationY = state.rotationY,
            showOuterLayer = state.showOuterLayer
        )

        val projectedFaces = faces
            .mapNotNull { face -> projectFace(face, centerX, centerY, scale) }
            .sortedBy { face ->
                // Outer layer draws after inner layer at same depth
                face.averageZ + if (face.isOuterLayer) OUTER_LAYER_Z_OFFSET else 0f
            }

        val bitmap = state.skinBitmap
        if (bitmap != null && pixelMap != null) {
            projectedFaces.forEach { projectedFace ->
                drawProjectedFace(projectedFace, bitmap, pixelMap)
            }
        }
    }
}

private fun projectFace(face: Face, centerX: Float, centerY: Float, scale: Float): ProjectedFace? {
    // Backface culling - skip faces pointing away from camera
    // Skip culling for outer layer - it follows inner layer visibility
    if (!face.isOuterLayer && face.normal().z < 0f) return null

    return ProjectedFace(
        points = face.vertices.map { vertex ->
            Offset(
                x = centerX + vertex.x * scale,
                y = centerY - vertex.y * scale
            )
        },
        averageZ = face.averageZ(),
        textureRect = face.getTextureRect(TEXTURE_SIZE, TEXTURE_SIZE),
        isOuterLayer = face.isOuterLayer
    )
}

private fun DrawScope.drawProjectedFace(
    face: ProjectedFace,
    skinBitmap: ImageBitmap,
    pixelMap: PixelMap
) {
    if (face.points.size < 4) return

    val textureRect = face.textureRect
    val texLeft = textureRect.left.roundToInt().coerceIn(0, skinBitmap.width - 1)
    val texTop = textureRect.top.roundToInt().coerceIn(0, skinBitmap.height - 1)
    val texRight = textureRect.right.roundToInt().coerceIn(texLeft + 1, skinBitmap.width)
    val texBottom = textureRect.bottom.roundToInt().coerceIn(texTop + 1, skinBitmap.height)
    val texWidth = texRight - texLeft
    val texHeight = texBottom - texTop

    if (texWidth <= 0 || texHeight <= 0) return

    val texelPath = Path()

    for (ty in 0 until texHeight) {
        for (tx in 0 until texWidth) {
            val color = pixelMap[texLeft + tx, texTop + ty]
            if (color.alpha == 0f) continue

            // Normalized UV with overlap to prevent gaps
            val u0 = tx.toFloat() / texWidth - TEXEL_OVERLAP
            val u1 = (tx + 1f) / texWidth + TEXEL_OVERLAP
            val v0 = ty.toFloat() / texHeight - TEXEL_OVERLAP
            val v1 = (ty + 1f) / texHeight + TEXEL_OVERLAP

            // Bilinear interpolation for quad corners
            val p0 = interpolateQuad(face.points, u0, v0)
            val p1 = interpolateQuad(face.points, u1, v0)
            val p2 = interpolateQuad(face.points, u1, v1)
            val p3 = interpolateQuad(face.points, u0, v1)

            texelPath.rewind()
            texelPath.moveTo(p0.x, p0.y)
            texelPath.lineTo(p1.x, p1.y)
            texelPath.lineTo(p2.x, p2.y)
            texelPath.lineTo(p3.x, p3.y)
            texelPath.close()

            drawPath(texelPath, color)
        }
    }
}

// Bilinear interpolation on a quad
// Vertex order: [0]=bottom-left, [1]=bottom-right, [2]=top-right, [3]=top-left
private fun interpolateQuad(points: List<Offset>, u: Float, v: Float): Offset {
    val bottomLeft = points[0]
    val bottomRight = points[1]
    val topRight = points[2]
    val topLeft = points[3]

    val top = Offset(
        x = topLeft.x + (topRight.x - topLeft.x) * u,
        y = topLeft.y + (topRight.y - topLeft.y) * u
    )
    val bottom = Offset(
        x = bottomLeft.x + (bottomRight.x - bottomLeft.x) * u,
        y = bottomLeft.y + (bottomRight.y - bottomLeft.y) * u
    )

    return Offset(
        x = top.x + (bottom.x - top.x) * v,
        y = top.y + (bottom.y - top.y) * v
    )
}
