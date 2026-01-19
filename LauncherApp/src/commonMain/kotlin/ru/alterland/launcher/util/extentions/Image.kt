package ru.alterland.launcher.util.extentions

import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.drawscope.CanvasDrawScope
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import coil3.Image

expect fun ByteArray.getImageBitmap(): ImageBitmap?

expect fun Image.toImageBitmap(): ImageBitmap

fun ImageBitmap.extractSkinFace(): ImageBitmap {
    val faceSize = 8
    val result = ImageBitmap(faceSize, faceSize)
    val canvas = Canvas(result)

    CanvasDrawScope().draw(
        density = Density(1f),
        layoutDirection = LayoutDirection.Ltr,
        canvas = canvas,
        size = androidx.compose.ui.geometry.Size(faceSize.toFloat(), faceSize.toFloat())
    ) {
        drawImage(
            image = this@extractSkinFace,
            srcOffset = IntOffset(8, 8),
            srcSize = IntSize(faceSize, faceSize),
            dstOffset = IntOffset.Zero,
            dstSize = IntSize(faceSize, faceSize),
            filterQuality = FilterQuality.None
        )

        if (this@extractSkinFace.width >= 64) {
            drawImage(
                image = this@extractSkinFace,
                srcOffset = IntOffset(40, 8),
                srcSize = IntSize(faceSize, faceSize),
                dstOffset = IntOffset.Zero,
                dstSize = IntSize(faceSize, faceSize),
                filterQuality = FilterQuality.None
            )
        }
    }

    return result
}
