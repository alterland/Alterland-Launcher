import androidx.compose.foundation.layout.Row
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
import org.koin.core.context.startKoin
import ru.alterland.launcher.App
import ru.alterland.launcher.di.commonModule
import ru.alterland.launcher.di.desktopModule
import java.awt.Dimension

fun main() = application {

    startKoin {
        modules(commonModule)
        modules(desktopModule)
    }

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
        WindowDraggableArea {
            Row(modifier = Modifier.fillMaxWidth().height(50.dp)) {}
        }

        this.window.minimumSize = Dimension(minWindowWidth, minWindowHeight)
        val rootPane = this.window.rootPane
        rootPane.putClientProperty("apple.awt.fullWindowContent", true)
        rootPane.putClientProperty("apple.awt.transparentTitleBar", true)
        rootPane.putClientProperty("apple.awt.windowTitleVisible", false)

        App()
    }
}
