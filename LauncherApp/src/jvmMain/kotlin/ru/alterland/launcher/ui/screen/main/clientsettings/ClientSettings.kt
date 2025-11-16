package ru.alterland.launcher.ui.screen.main.clientsettings

import alterlandlauncher.launcherapp.generated.resources.Res
import alterlandlauncher.launcherapp.generated.resources.settings_auto_connect
import alterlandlauncher.launcherapp.generated.resources.settings_launch_after_update
import alterlandlauncher.launcherapp.generated.resources.settings_launch_fullscreen
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import ru.alterland.launcher.ui.widgets.CheckBox
import java.util.*

@Composable
fun ClientSettings(
    state: ClientSettingsContract.State,
    onAction: (ClientSettingsContract.Action) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        CheckBox(
            checked = state.settings?.launchAfterUpdate ?: false,
            text = stringResource(Res.string.settings_launch_after_update),
            onClick = { onAction(ClientSettingsContract.Action.OnLaunchAfterUpdateClicked) }
        )
        CheckBox(
            checked = state.settings?.launchFullscreen ?: false,
            text = stringResource(Res.string.settings_launch_fullscreen),
            onClick = { onAction(ClientSettingsContract.Action.OnLaunchFullscreenClicked) }
        )
        CheckBox(
            checked = state.settings?.autoConnect ?: false,
            text = stringResource(Res.string.settings_auto_connect),
            onClick = { onAction(ClientSettingsContract.Action.OnAutoConnectClicked) }
        )

//        with(state.ramSlider) {
//            val ramLabel = formatRamLabel(value)
//            Text(
//                modifier = Modifier.padding(bottom = 8.dp),
//                text = "RAM: $ramLabel",
//                color = AppTheme.colors.labelPrimary
//            )
//            Slider(
//                value = value.toFloat(),
//                onValueChange = { onAction(ClientSettingsContract.Action.OnRamSliderValueChange(it)) },
//                onValueChangeFinished = { onAction(ClientSettingsContract.Action.OnRamSliderValueChangeFinished) },
//                valueRange = minValue.toFloat()..maxValue.toFloat(),
//                steps = steps,
//                colors = SliderDefaults.colors(
//                    thumbColor = AppTheme.colors.primary,
//                    activeTrackColor = AppTheme.colors.primary
//                )
//            )
//        }
    }
}

private fun formatRamLabel(valueMb: Int): String {
    val gbValue = valueMb / 1024f
    val hasFraction = valueMb % 1024 != 0
    val gbText = if (hasFraction) {
        String.format(Locale.US, "%.1f", gbValue)
    } else {
        (valueMb / 1024).toString()
    }
    return "$gbText GB ($valueMb MB)"
}
