package ru.alterland.launcher.ui.screen.main.skins

import ru.alterland.launcher.domain.model.Skin
import ru.alterland.launcher.ui.base.UiAction
import ru.alterland.launcher.ui.base.UiEffect
import ru.alterland.launcher.ui.base.UiState
import ru.alterland.launcher.ui.widgets.skinview.animation.AnimationType

class SkinsContract {
    sealed class Action : UiAction

    data class State(
        val selectedSkin: Skin? = null,
        val skins: List<Skin> = emptyList(),
        val selectedAnimation: AnimationType = AnimationType.WALKING,
        val selectedModelType: Skin.ModelType = Skin.ModelType.WIDE
    ): UiState

    sealed class Effect: UiEffect
}
