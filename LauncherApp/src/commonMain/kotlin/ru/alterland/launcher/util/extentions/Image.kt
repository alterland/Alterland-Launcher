package ru.alterland.launcher.util.extentions

import androidx.compose.ui.graphics.ImageBitmap
import coil3.Image

expect fun ByteArray.getImageBitmap(): ImageBitmap?

expect fun Image.toImageBitmap(): ImageBitmap
