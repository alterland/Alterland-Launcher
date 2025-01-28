package ru.alterland.launcher.ui.screen.main.skins

import ru.alterland.launcher.ui.base.UiEffect
import ru.alterland.launcher.ui.base.UiEvent
import ru.alterland.launcher.ui.base.UiState

import ru.alterland.launcher.domain.model.Skin


class SkinsContract {

    sealed class Event : UiEvent {
        data class ApplySkin(val skin: Skin) : Event()
        data class AddSkin(val path: String) : Event()
        data class RenameSkin(val skin: Skin?) : Event()
        data class FinishRename(val skin: Skin, val newName: String) : Event()
        data class DeleteSkin(val skin: Skin) : Event()

        data class UpdateNewName(val newName: String) : Event()
    }

    data class State(
        val currentSkin: Skin? = null,
        val skinLibrary: List<Skin> = emptyList(),
        val renamingSkin: Skin? = null,
        val newName: String = ""
        ): UiState

    sealed class Effect: UiEffect() {

    }
}