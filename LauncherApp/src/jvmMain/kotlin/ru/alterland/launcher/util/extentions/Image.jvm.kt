package ru.alterland.launcher.util.extentions

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import coil3.toBitmap
import org.jetbrains.skia.Image

actual fun ByteArray.getImageBitmap(): ImageBitmap? {
    val skiaImage = Image.makeFromEncoded(this)
    return skiaImage.toComposeImageBitmap()
}

actual fun coil3.Image.toImageBitmap(): ImageBitmap {
    val bitmap = toBitmap()
    return Image.makeFromBitmap(bitmap).toComposeImageBitmap()
}
