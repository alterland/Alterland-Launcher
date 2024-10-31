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
import ru.alterland.launcher.ui.widgets.CheckBox

import androidx.compose.material.*
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape

@Composable
fun ClientSettings(
    state: ClientSettingsContract.State,
    setEvent: (e: ClientSettingsContract.Event) -> Unit,
    navigateBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.colors.backgroundTertiary)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier.padding(bottom = 15.dp),
            text = stringResource(Res.string.settings),
            color = AppTheme.colors.labelPrimary,
            fontSize = 24.sp,
        )

        Divider(
            modifier = Modifier.padding(vertical = 8.dp),
            color = AppTheme.colors.labelPrimary.copy(alpha = 0.5f),
            thickness = 1.dp,
        )

        Text(
            text = stringResource(Res.string.jvm_settings),
            color = AppTheme.colors.labelPrimary,
            modifier = Modifier.padding(vertical = 8.dp),
            fontSize = 20.sp,
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
                    },
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Text(
                    text = "Выбрано значение: ${state.ramSettings.value}",
                    color = AppTheme.colors.labelPrimary,
                    modifier = Modifier.padding(top = 8.dp)
                    )
                Spacer(modifier = Modifier.height(10.dp))
                CheckBox(
                    checked = state.useRecommendedRamValue,
                    text = stringResource(Res.string.jvm_settings_use_recommended_ram),
                    ) {
                    setEvent(ClientSettingsContract.Event.OnUseRecommendedRamValueClick)
                }
            }
            is RamSettings.FixedRamSettings -> {
                Text(
                    text = "Выбрано дефолтное значение: ${state.ramSettings.value}",
                    color = AppTheme.colors.labelPrimary,
                )
            }
            null -> {}
        }

        Divider(
            modifier = Modifier.padding(vertical = 8.dp),
            color = AppTheme.colors.labelPrimary.copy(alpha = 0.5f),
            thickness = 1.dp
        )

//screen resolution
        var width by remember {mutableStateOf("")}
        var height by remember {mutableStateOf("")}

        Text(
            text = "Разрешение экрана",
            color = AppTheme.colors.labelPrimary,
            modifier = Modifier.padding(vertical = 8.dp),
            fontSize = 20.sp,
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = width,
                onValueChange = {input ->
                    if (input.matches(Regex("\\d*"))) {
                        width = input
                    }
                },
                modifier = Modifier
                    .width(100.dp)
//                    .height(30.dp)
                    .background(AppTheme.colors.backgroundElevatedPrimary, RoundedCornerShape(8.dp)),
                placeholder = if (!state.launchFullScreen) {
                    {
                        Box(
//                            modifier = Modifier.fillMaxHeight(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "1920",
                                color = AppTheme.colors.labelSecondary,
//                                fontSize = 10.sp
                            )
                        }
                    }
                } else null,
                textStyle = LocalTextStyle.current.copy(color = AppTheme.colors.labelPrimary),
                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = AppTheme.colors.labelQuaternary,
                    unfocusedIndicatorColor = AppTheme.colors.labelQuaternary,
                    cursorColor = AppTheme.colors.labelQuaternary
                ),
                enabled = !state.launchFullScreen
            )

            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "x", color = AppTheme.colors.labelSecondary)
            Spacer(modifier = Modifier.width(8.dp))

            TextField(
                value = height,
                onValueChange = {input ->
                    if (input.matches(Regex("\\d*"))) {
                        height = input
                    }
                },

                modifier = Modifier
                    .width(100.dp)
                    .background(AppTheme.colors.backgroundElevatedPrimary, RoundedCornerShape(8.dp)),
                placeholder = if (!state.launchFullScreen) {
                    { Text("1080", color = AppTheme.colors.labelSecondary) }
                } else null,
                textStyle = LocalTextStyle.current.copy(color = AppTheme.colors.labelPrimary),
                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = AppTheme.colors.labelQuaternary,
                    unfocusedIndicatorColor = AppTheme.colors.labelQuaternary,
                    cursorColor = AppTheme.colors.labelQuaternary
                ),
                enabled = !state.launchFullScreen
            )
        }
        Spacer(modifier = Modifier.height(15.dp))

//fullscreen
        CheckBox(
            checked = state.launchFullScreen,
            text = stringResource(Res.string.client_settings_launch_fullscreen),

        ) {
            setEvent(ClientSettingsContract.Event.OnLaunchFullScreen)
            if (!state.launchFullScreen) {
                width = "1920"
                height = "1080"
            }
        }

        Divider(
            modifier = Modifier.padding(vertical = 8.dp),
            color = AppTheme.colors.labelPrimary.copy(alpha = 0.5f),
            thickness = 1.dp
            )

//game settings
        Text(
            text = stringResource(Res.string.client_settings),
            color = AppTheme.colors.labelPrimary,
            fontSize = 20.sp,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        CheckBox(
            checked = state.launchAfterUpdate,
            text = stringResource(Res.string.client_settings_launch_after_update),
            ) {
            setEvent(ClientSettingsContract.Event.OnLaunchAfterUpdate)
        }

        Spacer(modifier = Modifier.height(8.dp))

        CheckBox(checked = state.autoConnect, text = stringResource(Res.string.client_settings_auto_connect)) {
            setEvent(ClientSettingsContract.Event.OnAutoConnect)
        }
        Divider(
            modifier = Modifier.padding(vertical = 8.dp),
            color = AppTheme.colors.labelPrimary.copy(alpha = 0.5f),
            thickness = 1.dp
            )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = navigateBack,
            modifier = Modifier.width(133.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = AppTheme.colors.labelSecondary),
            shape = RoundedCornerShape(10.dp)
        ) {
            Text("Применить", color = AppTheme.colors.labelPrimary)
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = navigateBack,
            modifier = Modifier
                .width(153.dp)
                .background(AppTheme.colors.backgroundElevatedPrimary),
            colors = ButtonDefaults.buttonColors(backgroundColor = AppTheme.colors.backgroundElevatedPrimary)
        ) {
            Text(text = stringResource(Res.string.back), color = AppTheme.colors.labelPrimary)
        }

    }
}
