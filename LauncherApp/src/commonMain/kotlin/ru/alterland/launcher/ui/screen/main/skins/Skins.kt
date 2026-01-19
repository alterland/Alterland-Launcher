package ru.alterland.launcher.ui.screen.main.skins

import alterlandlauncher.launcherapp.generated.resources.Res
import alterlandlauncher.launcherapp.generated.resources.skins_library
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.vinceglb.filekit.dialogs.FileKitMode
import io.github.vinceglb.filekit.dialogs.FileKitType
import io.github.vinceglb.filekit.dialogs.compose.rememberFilePickerLauncher
import io.github.vinceglb.filekit.path
import org.jetbrains.compose.resources.stringResource
import ru.alterland.launcher.ui.screen.main.skins.components.AddSkinButton
import ru.alterland.launcher.ui.screen.main.skins.components.SelectedSkinPanel
import ru.alterland.launcher.ui.screen.main.skins.components.SkinGridItem
import ru.alterland.launcher.ui.theme.AppTheme

@Composable
fun Skins(
    state: SkinsContract.State,
    onAction: (SkinsContract.Action) -> Unit
) {
    val imagePickerLauncher = rememberFilePickerLauncher(
        mode = FileKitMode.Single,
        type = FileKitType.Image
    ) { file ->
        file?.let {
            onAction(SkinsContract.Action.UploadCustomSkin(it.path))
        }
    }

    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.colors.backgroundElevatedTertiary)
    ) {
        state.user.getOrNull()?.skin?.let { skin ->
            SelectedSkinPanel(
                skin = skin,
                selectedAnimationType = state.selectedAnimationType,
                selectedModelType = state.selectedModelType,
                onAnimationTypeSelected = { onAction(SkinsContract.Action.SelectAnimationType(it)) },
                onModelTypeSelected = { onAction(SkinsContract.Action.SelectModelType(it)) }
            )
        }

        SkinsGrid(
            state = state,
            onSkinSelect = { onAction(SkinsContract.Action.SelectSkin(it)) },
            onAddSkinClick = { imagePickerLauncher.launch() }
        )
    }
}

@Composable
private fun SkinsGrid(
    modifier: Modifier = Modifier,
    state: SkinsContract.State,
    onSkinSelect: (ru.alterland.launcher.domain.model.Skin) -> Unit,
    onAddSkinClick: () -> Unit
) {
    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Adaptive(120.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(16.dp)
    ) {
        item(span = { GridItemSpan(maxLineSpan) }) {
            Text(
                text = stringResource(Res.string.skins_library),
                color = AppTheme.colors.labelPrimary,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                fontSize = 17.sp
            )
        }

        state.user.getOrNull()?.let { user ->
                item {
                    AddSkinButton(onClick = onAddSkinClick)
                }
        }

        item {
            AddSkinButton(onClick = onAddSkinClick)
        }

        items(state.skins) { skin ->
            SkinGridItem(
                skin = skin,
                onSelect = { onSkinSelect(skin) }
            )
        }
    }
}
