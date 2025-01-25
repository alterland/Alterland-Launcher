package ru.alterland.launcher.ui.screen.main.clientsettings

import cafe.adriel.voyager.core.screen.Screen
import ru.alterland.launcher.ui.screen.main.servers.client.ClientPayload

expect class ClientSettingsScreen constructor(payload: ClientPayload): Screen
