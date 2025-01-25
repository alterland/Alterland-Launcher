package ru.alterland.launcher.ui.screen.main.servers

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

sealed class ServerTab @OptIn(ExperimentalUuidApi::class) constructor(
    private val uniqueId: ScreenKey = Uuid.random().toString(),
    private val tabOptions: TabOptions,
    private val screen: Screen
): Tab {

    override val key: ScreenKey = uniqueId

    override val options: TabOptions
        @Composable
        get() = tabOptions

    @Composable
    override fun Content() {
        Navigator(screen)
    }

    data class PlayTab(
        private val tabOptions: TabOptions,
        private val screen: Screen
    ): ServerTab(
        tabOptions = tabOptions,
        screen = screen
    )

    data class ClientSettingsTab(
        private val tabOptions: TabOptions,
        private val screen: Screen
    ): ServerTab(
        tabOptions = tabOptions,
        screen = screen
    )

    data class EditServerTab(
        private val tabOptions: TabOptions,
        private val screen: Screen
    ): ServerTab(
        tabOptions = tabOptions,
        screen = screen
    )

    data class AddServerTab(
        private val tabOptions: TabOptions,
        private val screen: Screen
    ): ServerTab(
        tabOptions = tabOptions,
        screen = screen
    )
}
