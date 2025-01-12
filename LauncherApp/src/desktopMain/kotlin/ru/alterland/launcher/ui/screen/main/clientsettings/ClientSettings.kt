package ru.alterland.launcher.ui.screen.main.clientsettings

import alterlandlauncher.launcherapp.generated.resources.*
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
import ru.alterland.launcher.ui.widgets.Input

//import androidx.activity.compose.rememberLauncherForActivityResult
//import androidx.activity.result.contract.ActivityResultContracts


@Composable
fun ClientSettings(
    state: ClientSettingsContract.State,
    onEvent: (e: ClientSettingsContract.Event) -> Unit,
    navigateBack: () -> Unit
) {

//    val directoryPickerLauncher = rememberLauncherForActivityResult(
//        contract = ActivityResultContracts.OpenDocumentTree()
//    ) { uri: Uri? ->
//        uri?.let {
//            onEvent(ClientSettingsContract.Event.OnPathChange(it.toString()))
//        }
//    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.colors.backgroundTertiary)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
//        Text(
//            modifier = Modifier.padding(bottom = 15.dp),
//            text = stringResource(Res.string.settings),
//            color = AppTheme.colors.labelPrimary,
//            fontSize = 24.sp,
//        )

//directory
        Column(modifier = Modifier.padding(16.dp))
        {
            var directoryPath by remember {mutableStateOf("")}

            Text(
                text = "Директория",
                color = AppTheme.colors.labelPrimary,
                modifier = Modifier.padding(bottom = 8.dp),
                fontSize = 15.sp,
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Input(
//                    hint = "C:/users/user/alterland",
                    text = directoryPath,
                    singleLine = true,
                    onInput = { input ->
                        if (!state.defaultDirectory){
                            directoryPath = input
                            (ClientSettingsContract.Event.OnPathChange(input))
                        }},
                    modifier = Modifier
                        .weight(1f)
//                        .padding(end = 5.dp)
                )

                Button(
                    text = "Обзор...",
                    onClick = { onEvent(ClientSettingsContract.Event.OnBrowseDirectory) },
//                    onClick = { directoryPickerLauncher.launch(null)},
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
                    if (!state.defaultDirectory) {
                        directoryPath = "C:/users/user/alterland"
                    }
                }
            }

        }


//RAM
        Column(modifier = Modifier.padding(16.dp))
        {
            Text(
                text = stringResource(Res.string.jvm_settings),
                color = AppTheme.colors.labelPrimary,
                modifier = Modifier.padding(bottom = 8.dp),
                fontSize = 15.sp,
            )

            when (state.ramSettings) {
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
                            onEvent(ClientSettingsContract.Event.OnRamSliderValueSelected(sliderPosition))
                        },
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    CheckBox(
                        checked = state.useRecommendedRamValue,
                        text = stringResource(Res.string.jvm_settings_use_recommended_ram),
                    ) {
                        onEvent(ClientSettingsContract.Event.OnUseRecommendedRamValueClick)

                        if (!state.useRecommendedRamValue) {
                            sliderPosition = state.ramSettings.value
                        }
                    }
                    Text(
                        text = "Выбрано значение: ${state.ramSettings.value.toInt()} Mb",
                        color = AppTheme.colors.labelPrimary,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }

                is RamSettings.FixedRamSettings -> {}

                null -> {}
            }
        }

//screen resolution
        var width by remember {mutableStateOf("")}
        var height by remember {mutableStateOf("")}

        Column(modifier = Modifier.padding(16.dp))
        {
            Text(
                text = "Разрешение экрана",
                color = AppTheme.colors.labelPrimary,
                modifier = Modifier.padding(bottom = 5.dp),
                fontSize = 15.sp,
            )

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Input(
                    hint = "1920",
                    text = width,
                    enabled = !state.launchFullScreen,
                    singleLine = true,
                    onInput = { input ->
                        if (!state.launchFullScreen && input.matches(Regex("\\d*"))) {
                            width = input
                            (ClientSettingsContract.Event.OnWidthInput(input))
                        } },
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp),
                )

                Text(
                    text = "x",
                    color = Color.White,
                    fontSize = 15.sp
                )

                Spacer(modifier = Modifier.width(8.dp))

                Input(
                    hint = "1080",
                    text = height,
                    enabled = !state.launchFullScreen,
                    singleLine = true,
                    onInput = { input ->
                        if (!state.launchFullScreen && input.matches(Regex("\\d*"))){
                            height = input
                            (ClientSettingsContract.Event.OnHeightInput(input))
                        } },
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp)
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
                    if (!state.launchFullScreen) {
                        width = ""
                        height = ""
                    }
                }
            }
        }

//game settings
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.Start,
        ) {
            Text(
                text = stringResource(Res.string.client_settings),
                color = AppTheme.colors.labelPrimary,
                fontSize = 15.sp,
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

            Spacer(modifier = Modifier.height(20.dp))

            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Button(
                    text = stringResource(Res.string.back),
                    onClick =  navigateBack,
                    modifier = Modifier
                        .padding(top = 14.dp)
                        .width(155.dp)
                        .height(35.dp)
                )
            }
        }
    }
}
