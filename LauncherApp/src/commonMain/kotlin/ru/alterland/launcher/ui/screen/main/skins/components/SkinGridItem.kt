package ru.alterland.launcher.ui.screen.main.skins.components

import alterlandlauncher.launcherapp.generated.resources.Res
import alterlandlauncher.launcherapp.generated.resources.skins_apply
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.withFrameMillis
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import ru.alterland.launcher.domain.model.Skin
import ru.alterland.launcher.ui.theme.AppTheme
import ru.alterland.launcher.ui.widgets.skinview.SkinView
import ru.alterland.launcher.ui.widgets.skinview.rememberSkinViewState

private val shape = RoundedCornerShape(8.dp)

@Composable
fun SkinGridItem(
    skin: Skin,
    onSelect: () -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()
    val skinViewState = rememberSkinViewState(skin = skin, initialIsPaused = true)

    LaunchedEffect(isHovered) {
        skinViewState.isPaused = !isHovered
        if (isHovered) {
            var lastFrameTime = withFrameMillis { it }
            while (true) {
                val frameTime = withFrameMillis { it }
                val deltaTime = (frameTime - lastFrameTime) / 1000f
                lastFrameTime = frameTime
                skinViewState.rotationY += deltaTime * 0.5f
            }
        }
    }

    Box(
        modifier = modifier
            .height(200.dp)
            .background(color = AppTheme.colors.backgroundTertiary, shape = shape)
            .clip(shape)
            .hoverable(interactionSource)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = skin.name,
                color = AppTheme.colors.labelSecondary,
                textAlign = TextAlign.Center,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
            Spacer(modifier = Modifier.height(8.dp))
            SkinView(
                modifier = Modifier.size(width = 120.dp, height = 160.dp),
                state = skinViewState,
                enableDragRotation = false
            )
        }

        HoverOverlay(
            isHovered = isHovered,
            onSelect = onSelect
        )
    }
}

@Composable
private fun HoverOverlay(
    isHovered: Boolean,
    onSelect: () -> Unit,
    modifier: Modifier = Modifier
) {
    val overlayColor by animateColorAsState(
        targetValue = if (isHovered) Color.Black.copy(alpha = 0.2f) else Color.Transparent,
        animationSpec = tween(200)
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(overlayColor)
            .then(if (isHovered) Modifier.clickable(onClick = onSelect) else Modifier),
        contentAlignment = Alignment.Center
    ) {
        if (isHovered) {
            Text(
                text = stringResource(Res.string.skins_apply),
                color = Color.White,
                fontWeight = FontWeight.Medium
            )
        }
    }
}
