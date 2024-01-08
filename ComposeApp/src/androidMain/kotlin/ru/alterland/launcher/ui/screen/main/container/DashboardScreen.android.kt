package ru.alterland.launcher.ui.screen.main.container

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen

actual class DashboardScreen : Screen {

    @Composable
    override fun Content() {
        Text("Dashboard screen")
    }
}
