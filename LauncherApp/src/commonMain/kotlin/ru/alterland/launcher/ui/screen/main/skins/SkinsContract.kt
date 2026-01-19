package ru.alterland.launcher.ui.screen.main.skins

import ru.alterland.launcher.domain.model.Skin
import ru.alterland.launcher.domain.model.User
import ru.alterland.launcher.ui.base.UiAction
import ru.alterland.launcher.ui.base.UiEffect
import ru.alterland.launcher.ui.base.UiState
import ru.alterland.launcher.ui.widgets.skinview.animation.AnimationType
import ru.alterland.launcher.util.base.Resource

class SkinsContract {
    sealed class Action : UiAction {
        data class SelectSkin(val skin: Skin) : Action()
        data class SelectAnimationType(val animationType: AnimationType) : Action()
        data class SelectModelType(val modelType: Skin.ModelType) : Action()
        data class UploadCustomSkin(val path: String) : Action()
    }

    data class State(
        val user: Resource<User> = Resource.Idle(),
        val skins: List<Skin> = emptyList(),
        val selectedAnimationType: AnimationType = AnimationType.WALKING,
        val selectedModelType: Skin.ModelType = Skin.ModelType.WIDE,
        val isLoading: Boolean = false,
        val isSaving: Boolean = false
    ) : UiState

    sealed class Effect : UiEffect {
        data class ShowError(val message: String) : Effect()
    }
}
