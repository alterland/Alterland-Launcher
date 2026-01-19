package ru.alterland.launcher.ui.screen.main.editserver

import alterlandlauncher.launcherapp.generated.resources.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import ru.alterland.launcher.ui.theme.AppTheme
import ru.alterland.launcher.ui.widgets.*
import ru.alterland.launcher.util.base.Resource

@Composable
fun EditServer(
    state: EditServerContract.State,
    onAction: (EditServerContract.Action) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.colors.backgroundTertiary)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Input(
                hint = stringResource(Res.string.edit_server_title),
                text = state.serverProfile.title,
                onInput = { onAction(EditServerContract.Action.OnTitleInput(it)) },
                enabled = true,
                singleLine = true
            )

            Input(
                hint = stringResource(Res.string.edit_server_description),
                text = state.serverProfile.description,
                onInput = { onAction(EditServerContract.Action.OnDescriptionInput(it)) },
                enabled = true
            )

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Input(
                    modifier = Modifier.weight(0.5f),
                    hint = stringResource(Res.string.edit_server_ip),
                    text = state.serverProfile.ip,
                    onInput = { onAction(EditServerContract.Action.OnIPInput(it)) },
                    enabled = true,
                    singleLine = true
                )
                Input(
                    modifier = Modifier.weight(0.5f),
                    hint = stringResource(Res.string.edit_server_port),
                    text = state.serverProfile.port.toString(),
                    onInput = { onAction(EditServerContract.Action.OnPortInput(it)) },
                    enabled = true,
                    singleLine = true
                )
            }

            DropdownInput(
                hint = stringResource(Res.string.edit_server_client_profile),
                inputContent = { Text(text = state.serverProfile.title) },
                dropdownContent = {
                    when(state.clientProfileObjects) {
                        is Resource.Content -> {
                            state.clientProfileObjects.data.map {
                                DropdownItemWrapper {
                                    Text(text = it.key)
                                }
                            }
                        }
                        is Resource.Error -> DropdownItemWrapper {
                            Column {
                                Text(text = "ошибка")
                            }
                        }
                        is Resource.Loading -> DropdownItemWrapper {
                            AppleCircularProgressIndicator(
                                color =  AppTheme.colors.forceWhitePrimary,
                                strokeWidth = 1.dp,
                                modifier = Modifier.padding(start = 8.dp).size(13.dp)
                            )
                        }
                        else -> {}
                    }
                }
            )

            state.serverProfile.clientProfile?.let {
                Button(
                    text = stringResource(Res.string.edit_server_client_profile_edit),
                    onClick = {  },
                    modifier = Modifier.padding(top = 14.dp).height(35.dp)
                )
            }
        }

        Button(
            text = stringResource(Res.string.save),
            isLoading = state.isSaveInProgress,
            onClick = { onAction(EditServerContract.Action.OnSaveClick) },
            modifier = Modifier
                .padding(top = 14.dp)
                .width(155.dp)
                .height(35.dp)
        )
    }
}
