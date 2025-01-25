package ru.alterland.launcher.ui.screen.main.editserver

import ru.alterland.launcher.domain.model.ServerProfile

sealed class EditServerPayload {
    data object Add: EditServerPayload()
    data class Edit(val serverProfile: ServerProfile): EditServerPayload()
}
