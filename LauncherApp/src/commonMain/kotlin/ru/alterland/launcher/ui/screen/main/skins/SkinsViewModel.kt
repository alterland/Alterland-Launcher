package ru.alterland.launcher.ui.screen.main.skins

import kotlinx.coroutines.launch
import org.orbitmvi.orbit.viewmodel.container
import ru.alterland.launcher.domain.repository.SkinRepository
import ru.alterland.launcher.ui.base.BaseViewModel

class SkinsViewModel(
    private val skinRepository: SkinRepository,
    private val payload: SkinsPayload
): BaseViewModel<SkinsContract.State, SkinsContract.Effect, SkinsContract.Action>() {

    override val container = container<SkinsContract.State, SkinsContract.Effect>(SkinsContract.State()) {
        loadSkin()
        loadSkins()
    }

    override fun dispatch(action: SkinsContract.Action) {

    }

    private fun loadSkin() = intent {
        viewModelScopeErrorHandled.launch {
            val skin = skinRepository.loadSkin(payload.serverProfile.id)
            reduce { state.copy(selectedSkin = skin) }
        }
    }

    private fun loadSkins() = intent {
        viewModelScopeErrorHandled.launch {
            val skins = skinRepository.loadSkins(payload.serverProfile.id)
            reduce { state.copy(skins = skins) }
        }
    }
}
