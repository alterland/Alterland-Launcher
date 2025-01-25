package ru.alterland.launcher.ui.screen.main.clientsettings

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import ru.alterland.launcher.ui.screen.main.servers.client.ClientPayload

actual class ClientSettingsScreen actual constructor(private val payload: ClientPayload): Screen {

    @Composable
    override fun Content() {}

}
