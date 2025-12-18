package ru.alterland.launcher.ui.screen.main.clientsettings

import alterlandlauncher.launcherapp.generated.resources.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.stringResource
import ru.alterland.launcher.domain.model.Skin
import ru.alterland.launcher.ui.theme.AppTheme
import ru.alterland.launcher.ui.widgets.Button
import ru.alterland.launcher.ui.widgets.CheckBox
import ru.alterland.launcher.ui.widgets.Input
import ru.alterland.launcher.ui.widgets.RadioButton
import ru.alterland.launcher.ui.widgets.player.PlayerModel3D

@Composable
fun ClientSettings(
    state: ClientSettingsContract.State,
    onAction: (ClientSettingsContract.Action) -> Unit
) {
    val focusRequester = remember { FocusRequester() }

    Row(
        modifier = Modifier
            .fillMaxSize()
            .clickable(
                interactionSource = MutableInteractionSource(),
                indication = null,
                onClick = { focusRequester.freeFocus() }
            )
            .padding(start = 24.dp, end = 16.dp, top = 32.dp, bottom = 32.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            state.skin?.let { skin ->
                PlayerModel3D(
                    modifier = Modifier
                        .size(150.dp, 200.dp)
                        .background(AppTheme.colors.backgroundElevatedSecondary),
                    skin = skin
                )
//                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
//                    RadioButton(
//                        selected = skin.modelType == Skin.ModelType.WIDE,
//                        text = stringResource(Res.string.settings_model_wide),
//                        onClick = { onAction(ClientSettingsContract.Action.Mode(Skin.ModelType.WIDE)) }
//                    )
//                    RadioButton(
//                        selected = skin.modelType == Skin.ModelType.SLIM,
//                        text = stringResource(Res.string.settings_model_slim),
//                        onClick = { onAction(ClientSettingsContract.Action.OnModelTypeChanged(Skin.ModelType.SLIM)) }
//                    )
//                }
//                Button(
//                    text = stringResource(Res.string.settings_select_skin),
//                    modifier = Modifier.width(100.dp),
//                    onClick = { onAction(ClientSettingsContract.Action.OnSelectSkinClicked) }
//                )
            }
            Button(
                modifier = Modifier.width(100.dp),
                text = stringResource(Res.string.settings_select_skin),
                onClick = { onAction(ClientSettingsContract.Action.OnChangeSkinClicked) }
            )
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .clickable(
                    interactionSource = MutableInteractionSource(),
                    indication = null,
                    onClick = { focusRequester.freeFocus() }
                ),
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
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(Res.string.settings_ram),
                        color = AppTheme.colors.labelPrimary
                    )
                    Input(
                        modifier = Modifier
                            .width(86.dp)
                            .focusRequester(focusRequester)
                            .onFocusChanged {
                                if (!it.hasFocus) {
                                    onAction(ClientSettingsContract.Action.OnRamInputFinished)
                                }
                            },
                        text = state.ramValue,
                        hint = stringResource(Res.string.settings_value),
                        singleLine = true,
                        onInput = { onAction(ClientSettingsContract.Action.OnRamInput(value = it)) }
                    )
                    Text(
                        text = stringResource(Res.string.megabytes),
                        color = AppTheme.colors.labelPrimary
                    )
                }
                Text(
                    text = stringResource(Res.string.settings_ram_warning, state.recommendedRam),
                    color = AppTheme.colors.labelSecondary,
                    fontSize = 10.sp
                )
            }
        }
    }
}
