package ru.alterland.launcher.ui.screen.main.skins.components

import alterlandlauncher.launcherapp.generated.resources.Res
import alterlandlauncher.launcherapp.generated.resources.ic_pause
import alterlandlauncher.launcherapp.generated.resources.ic_play_arrow
import alterlandlauncher.launcherapp.generated.resources.ic_play_fast_forward
import alterlandlauncher.launcherapp.generated.resources.selected_skin
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import ru.alterland.launcher.domain.model.Skin
import ru.alterland.launcher.ui.theme.AppTheme
import ru.alterland.launcher.ui.widgets.skinview.SkinView
import ru.alterland.launcher.ui.widgets.skinview.animation.AnimationType
import ru.alterland.launcher.ui.widgets.skinview.rememberSkinViewState

@Composable
fun SelectedSkinPanel(
    skin: Skin,
    selectedAnimationType: AnimationType,
    selectedModelType: Skin.ModelType,
    onAnimationTypeSelected: (AnimationType) -> Unit,
    onModelTypeSelected: (Skin.ModelType) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(top = 16.dp)
            .width(220.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(Res.string.selected_skin),
                color = AppTheme.colors.labelPrimary,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                fontSize = 17.sp
            )

            val skinViewState = rememberSkinViewState(
                initialAnimation = selectedAnimationType,
                skin = skin
            )

            LaunchedEffect(selectedAnimationType) {
                skinViewState.animationType = selectedAnimationType
            }

            LaunchedEffect(selectedModelType) {
                skinViewState.modelType = selectedModelType
            }

            SkinView(
                modifier = Modifier.size(width = 120.dp, height = 240.dp),
                state = skinViewState
            )

            AnimationControls(
                selectedAnimationType = selectedAnimationType,
                onAnimationTypeSelected = onAnimationTypeSelected
            )

            ModelTypeSwitch(
                modifier = Modifier.padding(top = 8.dp),
                selectedModelType = selectedModelType,
                onModelTypeSelected = onModelTypeSelected
            )
        }
    }
}

@Composable
private fun AnimationControls(
    selectedAnimationType: AnimationType,
    onAnimationTypeSelected: (AnimationType) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center
    ) {
        AnimationButton(
            painter = painterResource(Res.drawable.ic_pause),
            isSelected = selectedAnimationType == AnimationType.IDLE,
            onClick = { onAnimationTypeSelected(AnimationType.IDLE) }
        )
        Spacer(modifier = Modifier.width(4.dp))
        AnimationButton(
            painter = painterResource(Res.drawable.ic_play_arrow),
            isSelected = selectedAnimationType == AnimationType.WALKING,
            onClick = { onAnimationTypeSelected(AnimationType.WALKING) }
        )
        Spacer(modifier = Modifier.width(4.dp))
        AnimationButton(
            painter = painterResource(Res.drawable.ic_play_fast_forward),
            isSelected = selectedAnimationType == AnimationType.RUNNING,
            onClick = { onAnimationTypeSelected(AnimationType.RUNNING) }
        )
    }
}
