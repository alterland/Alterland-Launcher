package ru.alterland.launcher.ui.screen.main.servers

import ru.alterland.launcher.ui.screen.main.clientsettings.ClientSettingsPayload
import ru.alterland.launcher.ui.screen.main.editserver.EditServerPayload
import ru.alterland.launcher.ui.screen.main.server.ServerPayload
import ru.alterland.launcher.ui.screen.main.skins.SkinsPayload

sealed class ServerTab {
    data class Server(val payload: ServerPayload): ServerTab()
    data class ClientSettings(val payload: ClientSettingsPayload): ServerTab()
    data class Skins(val payload: SkinsPayload): ServerTab()
    data class EditServer(val payload: EditServerPayload): ServerTab()
    data class AddServer(val payload: EditServerPayload): ServerTab()
}
