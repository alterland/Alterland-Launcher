package ru.alterland.launcher.ui.screen.main.editserver

import kotlinx.serialization.Serializable
import ru.alterland.launcher.domain.model.ServerProfile

@Serializable
sealed class EditServerMode {

    @Serializable
    data object Add: EditServerMode()

    @Serializable
    data class Edit(val serverProfile: ServerProfile): EditServerMode()
}
