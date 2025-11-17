package ru.alterland.launcher.util.extentions

import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventType

actual fun Modifier.onPointerEvent(
    type: PointerEventType,
    onEvent: () -> Unit
): Modifier = this
