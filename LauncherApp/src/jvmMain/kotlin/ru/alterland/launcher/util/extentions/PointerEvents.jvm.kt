package ru.alterland.launcher.util.extentions

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent as composeOnPointerEvent

@OptIn(ExperimentalComposeUiApi::class)
actual fun Modifier.onPointerEvent(
    type: PointerEventType,
    onEvent: () -> Unit
): Modifier = composeOnPointerEvent(type) { onEvent() }
