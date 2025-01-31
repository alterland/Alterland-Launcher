package ru.alterland.launcher.ui.screen.main.skins

import cafe.adriel.voyager.core.model.screenModelScope
import cafe.adriel.voyager.core.registry.screenModule
import ru.alterland.launcher.ui.base.BaseScreenModel
import ru.alterland.launcher.domain.repository.SkinRepository
import ru.alterland.launcher.domain.model.Skin
import ru.alterland.launcher.util.extentions.launchSafe

class SkinsScreenModel(
    private val skinRepository: SkinRepository
) : BaseScreenModel<SkinsContract.Event, SkinsContract.State, SkinsContract.Effect>(
    initialState = SkinsContract.State()
) {

    init {
        loadSkins()
    }

    override fun onEvent(event: SkinsContract.Event) {
        when(event) {
            is SkinsContract.Event.ApplySkin -> setState { copy(currentSkin = event.skin) }
            is SkinsContract.Event.AddSkin -> addSkin(event.path)
            is SkinsContract.Event.RenameSkin -> setState { copy(renamingSkin = event.skin, newName = "") }
            is SkinsContract.Event.UpdateNewName -> setState { copy(newName = event.newName) }
            is SkinsContract.Event.FinishRename -> renameSkin(event.skin, event.newName)
            is SkinsContract.Event.DeleteSkin -> deleteSkin(event.skin)
//            is SkinsContract.Event.ToggleHover -> { setState { copy(hoveredSkin = if (hoveredSkin == event.skin) null else event.skin) }}
            is SkinsContract.Event.ToggleHover -> handleToggleHover(event.skin)
        }
    }

    private fun handleToggleHover(skin: Skin) {
        setState { copy(hoveredSkin = if (hoveredSkin == skin) null else skin) }
    }

    private fun addSkin(path: String) = screenModelScope.launchSafe(::onError) {
        skinRepository.addSkin(path)
        loadSkins()
    }

    private fun loadSkins() = screenModelScope.launchSafe(::onError){
        val skins = skinRepository.getSkins()
        setState { copy(skinLibrary = skins)}
    }

    private fun renameSkin(skin: Skin, newName: String) = screenModelScope.launchSafe(::onError) {
        skinRepository.renameSkin(skin, newName)
        loadSkins()
        setState { copy(renamingSkin = null) }
    }

    private fun deleteSkin(skin: Skin) = screenModelScope.launchSafe(::onError){
        skinRepository.deleteSkin(skin)
        loadSkins()
    }

}