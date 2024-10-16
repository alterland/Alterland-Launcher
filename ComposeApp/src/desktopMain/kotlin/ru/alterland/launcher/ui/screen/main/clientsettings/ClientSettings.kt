package ru.alterland.launcher.ui.screen.main.clientsettings

import alterlandlauncher.composeapp.generated.resources.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material.Slider
import androidx.compose.material.SliderDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import ru.alterland.launcher.ui.theme.AppTheme
import ru.alterland.launcher.ui.widgets.Button
import ru.alterland.launcher.ui.widgets.CheckBox

@Composable
fun ClientSettings(
    state: ClientSettingsContract.State,
    setEvent: (e: ClientSettingsContract.Event) -> Unit,
    navigateBack: () -> Unit
) {

    Column(
        modifier = Modifier.fillMaxSize().background(AppTheme.colors.backgroundTertiary),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(Res.string.settings),
            color = AppTheme.colors.labelPrimary,
        )
        Text(
            text = stringResource(Res.string.jvm_settings),
            color = AppTheme.colors.labelPrimary,
        )
        when(state.ramSettings) {
            is RamSettings.CustomRamSettings -> {
                var sliderPosition by remember { mutableFloatStateOf(state.ramSettings.value) }
                Slider(
                    value = sliderPosition,
                    steps = state.ramSettings.steps,
                    enabled = !state.useRecommendedRamValue,
                    valueRange = state.ramSettings.min..state.ramSettings.max,
                    colors = SliderDefaults.colors(
                        thumbColor = AppTheme.colors.primary,
                        activeTrackColor = AppTheme.colors.primary,
                        inactiveTrackColor = AppTheme.colors.backgroundElevatedPrimary,
                    ),
                    onValueChange = { sliderPosition = it },
                    onValueChangeFinished = {
                        setEvent(ClientSettingsContract.Event.OnRamSliderValueSelected(sliderPosition))
                    }
                )
                Text(text = "Выбрано значение: ${state.ramSettings.value}")
                CheckBox(checked = state.useRecommendedRamValue, text = stringResource(Res.string.jvm_settings_use_recommended_ram)) {
                    setEvent(ClientSettingsContract.Event.OnUseRecommendedRamValueClick)
                }
            }
            is RamSettings.FixedRamSettings -> {

            }
            null -> {}
        }
        Text(
            text = stringResource(Res.string.client_settings),
            color = AppTheme.colors.labelPrimary,
        )
        CheckBox(checked = true, text = stringResource(Res.string.client_settings_launch_after_update)) {

        }
        CheckBox(checked = true, text = stringResource(Res.string.client_settings_launch_fullscreen)) {

        }
        CheckBox(checked = true, text = stringResource(Res.string.client_settings_auto_connect)) {

        }
        Button(
            text = stringResource(Res.string.back),
            backgroundColor = AppTheme.colors.backgroundElevatedPrimary,
            modifier = Modifier.width(153.dp),
            onClick = navigateBack
        )
    }
}
