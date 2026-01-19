package ru.alterland.launcher.ui.screen.main.skins

import kotlinx.coroutines.launch
import org.orbitmvi.orbit.viewmodel.container
import ru.alterland.launcher.domain.model.Skin
import ru.alterland.launcher.domain.repository.SkinRepository
import ru.alterland.launcher.domain.repository.UserRepository
import ru.alterland.launcher.ui.base.BaseViewModel
import ru.alterland.launcher.ui.widgets.skinview.animation.AnimationType

class SkinsViewModel(
    private val skinRepository: SkinRepository,
    private val userRepository: UserRepository
) : BaseViewModel<SkinsContract.State, SkinsContract.Effect, SkinsContract.Action>() {

    override val container = container<SkinsContract.State, SkinsContract.Effect>(SkinsContract.State()) {
        subscribeToUser()
        loadLibrary()
    }

    override fun dispatch(action: SkinsContract.Action) {
        when (action) {
            is SkinsContract.Action.SelectSkin -> handleSelectSkin(action.skin)
            is SkinsContract.Action.SelectAnimationType -> handleSelectAnimationType(action.animationType)
            is SkinsContract.Action.SelectModelType -> handleSelectModelType(action.modelType)
            is SkinsContract.Action.UploadCustomSkin -> handleUploadCustomSkin(action.path)
        }
    }

    private fun subscribeToUser() = intent {
        userRepository.user.collect { resource ->
            reduce {
                state.copy(
                    user = resource,
                    //selectedModelType = data.skin?.modelType ?: Skin.ModelType.WIDE
                )
            }
        }
    }

    private fun loadLibrary() = intent {
        reduce { state.copy(isLoading = true) }
        viewModelScopeErrorHandled.launch {
            try {
                val skins = skinRepository.getLibrarySkins()
                reduce {
                    state.copy(
                        skins = skins,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                reduce { state.copy(isLoading = false) }
                postSideEffect(SkinsContract.Effect.ShowError(e.message ?: "Failed to load skins"))
            }
        }
    }

    private fun handleSelectSkin(skin: Skin) = intent {
        reduce { state.copy(isSaving = true) }
        viewModelScopeErrorHandled.launch {
            runCatching {
                skinRepository.setUserSkin(skin.id)
                userRepository.updateUser()
                reduce { state.copy(isSaving = false) }
            }.onFailure {
                reduce { state.copy(isSaving = false) }
            }
        }
    }

    private fun handleSelectAnimationType(animationType: AnimationType) = intent {
        reduce { state.copy(selectedAnimationType = animationType) }
    }

    private fun handleSelectModelType(modelType: Skin.ModelType) = intent {
        reduce { state.copy(selectedModelType = modelType) }
    }

    private fun handleUploadCustomSkin(path: String) = intent {
        viewModelScopeErrorHandled.launch {
            skinRepository.uploadCustomSkin(path, state.selectedModelType)
            userRepository.updateUser()
        }
    }
}
