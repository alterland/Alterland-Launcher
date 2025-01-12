package ru.alterland.launcher.ui.base

import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

open class UiEffect {
    @OptIn(ExperimentalUuidApi::class)
    val uniqueId: String = Uuid.random().toString()
}
