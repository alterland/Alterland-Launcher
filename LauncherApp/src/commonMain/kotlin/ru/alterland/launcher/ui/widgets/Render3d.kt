package ru.alterland.launcher.ui.widgets

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
expect fun Render3d(
    modifier: Modifier = Modifier,
    modelBytes: ByteArray = byteArrayOf()
)
