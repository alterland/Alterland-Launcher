package ru.alterland.launcher

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.window.WindowDraggableArea
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import io.github.vinceglb.filekit.FileKit
import org.koin.compose.KoinApplication
import ru.alterland.launcher.di.commonModule
import ru.alterland.launcher.di.desktopModule
import java.awt.Dimension

fun main() = application {
    KoinApplication(application = {
        modules(commonModule)
        modules(desktopModule)
    }) {
        FileKit.init(appId = BuildConfig.WORK_FOLDER)

        val minWindowWidth = 800
        val minWindowHeight = 530

        val state = rememberWindowState(
            position = WindowPosition.Aligned(Alignment.Center),
            width = minWindowWidth.dp,
            height = minWindowHeight.dp
        )

        Window(
            onCloseRequest = ::exitApplication,
            undecorated = false,
            resizable = true,
            state = state,
            title = ""
        ) {
            with(window) {
                minimumSize = Dimension(minWindowWidth, minWindowHeight)
                rootPane.putClientProperty("apple.awt.fullWindowContent", true)
                rootPane.putClientProperty("apple.awt.transparentTitleBar", true)
                rootPane.putClientProperty("apple.awt.windowTitleVisible", false)
            }

            WindowDraggableArea(modifier = Modifier.fillMaxWidth().height(50.dp))
            App()
        }
    }
}
