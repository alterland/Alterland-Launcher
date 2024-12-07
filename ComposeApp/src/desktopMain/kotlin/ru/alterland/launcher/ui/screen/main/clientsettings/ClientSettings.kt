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
import ru.alterland.launcher.ui.widgets.Button

import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.*
import androidx.compose.ui.graphics.Color
import io.github.vinceglb.filekit.compose.rememberDirectoryPickerLauncher
import ru.alterland.launcher.ui.widgets.Input

@Composable
fun ClientSettings(
    state: ClientSettingsContract.State,
    onEvent: (e: ClientSettingsContract.Event) -> Unit,
    navigateBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.colors.backgroundTertiary)
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

//directory
        Column(modifier = Modifier.padding(10.dp))
        {
//            var directoryPath by remember { mutableStateOf("") }

            Text(
                text = "Директория",
                color = AppTheme.colors.labelPrimary,
                modifier = Modifier.padding(bottom = 8.dp),
                fontSize = 12.sp,
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Input(
//                    hint = state.directoryPath,
                    text = state.directoryPath,
                    enabled = !state.defaultDirectory,
                    singleLine = true,
                    onInput = { input ->
                        if (!state.defaultDirectory) {
                            onEvent(ClientSettingsContract.Event.OnPathChange(input))
                        }
                    },
                    modifier = Modifier
                        .weight(1f)
                        .size(32.dp)
                )

                val launcher = rememberDirectoryPickerLauncher(
                    title = "Pick"
                ) { directory ->
                    directory?.let {
//                        directoryPath = it.path.toString()
                        onEvent(ClientSettingsContract.Event.OnPathChange(it.path.toString()))
                    }
                }

                Button(
                    text = "Обзор...",
                    onClick = { launcher.launch() },
                    modifier = Modifier
                        .height(32.dp)
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 8.dp)
            ) {
                CheckBox(
                    checked = state.defaultDirectory,
                    modifier = Modifier.padding(end = 8.dp),
                    text = stringResource(Res.string.client_settings_default_directory),
                ) {
                    onEvent(ClientSettingsContract.Event.OnDefaultDirectory)
//                    directoryPath = if (!state.defaultDirectory) {
//                        "C:/users/user/alterland"
//                    } else {
//                        ""
//                    }
                }
            }
//RAM
            Column(modifier = Modifier.padding(10.dp))
            {
                Text(
                    text = stringResource(Res.string.jvm_settings),
                    color = AppTheme.colors.labelPrimary,
                    fontSize = 12.sp,
                )

                when (state.ramSettings) {
                    is RamSettings.CustomRamSettings -> {
                        var sliderPosition by remember { mutableFloatStateOf(state.ramSettings.value) }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {

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
                                onValueChange = {
                                    sliderPosition = it},
                                onValueChangeFinished = {
                                    onEvent(ClientSettingsContract.Event.OnRamSliderValueSelected(sliderPosition))
                                },
                                modifier = Modifier.weight(1f)
                            )
                            Input(
                                text = "${state.ramSettings.value.toInt()}",
//                                enabled = !state.useRecommendedRamValue,
                                enabled = false,
                                singleLine = true,
                                onInput = { input ->
                                    if (!state.useRecommendedRamValue && input.matches(Regex("\\d*"))) {
                                        (ClientSettingsContract.Event.OnInputRamSettings(input))
                                    } },
                                modifier = Modifier
                                    .width(65.dp)
                            )
                        }
                        CheckBox(
                            checked = state.useRecommendedRamValue,
                            text = stringResource(Res.string.jvm_settings_use_recommended_ram),
                        ) {
                            onEvent(ClientSettingsContract.Event.OnUseRecommendedRamValueClick)
                            if (!state.useRecommendedRamValue) {
                                sliderPosition = state.ramSettings.value
                            }
                        }
                    }

                    is RamSettings.FixedRamSettings -> {}

                    null -> {}
                }
            }

//screen resolution

            Column(modifier = Modifier.padding(10.dp))
            {
                Text(
                    text = "Разрешение экрана",
                    color = AppTheme.colors.labelPrimary,
                    modifier = Modifier.padding(bottom = 5.dp),
                    fontSize = 12.sp,
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Input(
                        text = state.width,
                        enabled = !state.launchFullScreen,
                        singleLine = true,
                        onInput = { input ->
                            if (!state.launchFullScreen && input.matches(Regex("\\d*"))) {
//                                width = input
                                onEvent(ClientSettingsContract.Event.OnWidthInput(input))
                            }
                        },
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 8.dp)
                            .size(32.dp)
                    )

                    Text(
                        text = "x",
                        color = Color.White,
                        fontSize = 12.sp
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Input(
                        text = state.height,
                        enabled = !state.launchFullScreen,
                        singleLine = true,
                        onInput = { input ->
                            if (!state.launchFullScreen && input.matches(Regex("\\d*"))) {
//                                height = input
                                onEvent(ClientSettingsContract.Event.OnHeightInput(input))
                            }
                        },
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 8.dp)
                            .size(32.dp)
                    )
                }

//fullscreen
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 8.dp)
                ) {

                    CheckBox(
                        checked = state.launchFullScreen,
                        modifier = Modifier.padding(end = 8.dp),
                        text = stringResource(Res.string.client_settings_launch_fullscreen),
                    ) {
                        onEvent(ClientSettingsContract.Event.OnLaunchFullScreen)
                    }
                }
            }

//game settings
            Column(
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.Start,
            ) {
                Text(
                    text = stringResource(Res.string.client_settings),
                    color = AppTheme.colors.labelPrimary,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                CheckBox(
                    checked = state.launchAfterUpdate,
                    text = stringResource(Res.string.client_settings_launch_after_update),
                ) {
                    onEvent(ClientSettingsContract.Event.OnLaunchAfterUpdate)
                }

                Spacer(modifier = Modifier.height(8.dp))

                CheckBox(checked = state.autoConnect, text = stringResource(Res.string.client_settings_auto_connect)) {
                    onEvent(ClientSettingsContract.Event.OnAutoConnect)
                }

                Spacer(modifier = Modifier.height(12.dp))

                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Button(
                        text = stringResource(Res.string.back),
                        onClick = navigateBack,
                        modifier = Modifier
                            .padding(top = 12.dp)
                            .width(155.dp)
                            .height(35.dp)
                    )
                }
            }
        }
    }
}

