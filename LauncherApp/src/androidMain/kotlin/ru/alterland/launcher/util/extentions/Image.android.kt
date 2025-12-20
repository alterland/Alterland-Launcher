package ru.alterland.launcher.util.extentions

import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import coil3.toBitmap

actual fun ByteArray.getImageBitmap(): ImageBitmap? {
    val bitmap = BitmapFactory.decodeByteArray(this, 0, size)
    return bitmap?.asImageBitmap()
}

actual fun coil3.Image.toImageBitmap(): ImageBitmap {
    return toBitmap().asImageBitmap()
}
