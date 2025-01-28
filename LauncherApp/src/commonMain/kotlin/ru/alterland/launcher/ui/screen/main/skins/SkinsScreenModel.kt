package ru.alterland.launcher.ui.screen.main.skins

import cafe.adriel.voyager.core.model.screenModelScope
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

            is SkinsContract.Event.RenameSkin -> setState { copy(renamingSkin = event.skin) }

            is SkinsContract.Event.UpdateNewName -> setState { copy(newName = event.newName) }

            is SkinsContract.Event.FinishRename -> {
                val updatedLibrary = state.value.skinLibrary.map {
                    if (it == event.skin) it.copy(name = event.newName) else it
                }
                setState { copy(skinLibrary = updatedLibrary, renamingSkin = null) }
            }
//            is ClientSkinSettingsContract.Event.DeleteSkin -> setState { copy(skinLibrary = state.value.skinLibrary.filter { it != event.skin}) }
            is SkinsContract.Event.DeleteSkin -> deleteSkin(event.skin)
        }
    }

    private fun addSkin(path: String) = screenModelScope.launchSafe(::onError) {
        skinRepository.addSkin(path)
        loadSkins()
    }

    private fun loadSkins() = screenModelScope.launchSafe(::onError){
        val skins = skinRepository.getSkins()
        setState { copy(skinLibrary = skins)}
    }

//    private fun deleteSkin(skin: Skin) = screenModelScope.launchSafe(::onError) {
//        skinRepository.deleteSkin(skin)
//        loadSkins()
//    }

    private fun deleteSkin(skin: Skin) = setState {
        copy(skinLibrary = skinLibrary.filterNot { it == skin })
    }

}