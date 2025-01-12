package ru.alterland.launcher.ui.screen.main.servers.client

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen

actual class ClientScreen actual constructor(private val payload: ClientPayload): Screen {

    @Composable
    override fun Content() {}
}
