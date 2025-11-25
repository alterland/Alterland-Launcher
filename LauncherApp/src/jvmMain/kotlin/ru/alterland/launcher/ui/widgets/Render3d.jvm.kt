package ru.alterland.launcher.ui.widgets

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.SwingPanel
import androidx.compose.ui.window.rememberWindowState
import org.jetbrains.compose.resources.ExperimentalResourceApi
import ru.alterland.launcher.ui.theme.AppTheme
import ru.alterland.launcher.util.toAwtColor
import javax.swing.SwingUtilities

@OptIn(ExperimentalResourceApi::class)
@Composable
actual fun Render3d(
    modifier: Modifier,
    modelBytes: ByteArray
) {
    val backgroundColor = AppTheme.colors.backgroundElevatedSecondary.toAwtColor()
    var canvas = remember<LWJGLCanvas?> { null }
    DisposableEffect(Unit) {
        onDispose {
            canvas?.destroy()
        }
    }

    SwingPanel(
        modifier = modifier,
        factory = {
            val instance = LWJGLCanvas(
                backgroundColor = backgroundColor
            )
            canvas = instance
            instance
        },
        update = {
            val renderLoop = object : Runnable {
                override fun run() {
                    if (it.isValid) {
                        it.paint()
                    }
                    if (it.isDisplayable) {
                        SwingUtilities.invokeLater(this)
                    }
                }
            }
            SwingUtilities.invokeLater(renderLoop)
        }
    )
}
