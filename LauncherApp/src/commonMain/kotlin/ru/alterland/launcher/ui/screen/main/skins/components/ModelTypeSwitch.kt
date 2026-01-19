package ru.alterland.launcher.ui.screen.main.skins.components

import alterlandlauncher.launcherapp.generated.resources.Res
import alterlandlauncher.launcherapp.generated.resources.skins_model_type_slim
import alterlandlauncher.launcherapp.generated.resources.skins_model_type_wide
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.stringResource
import ru.alterland.launcher.domain.model.Skin
import ru.alterland.launcher.ui.theme.AppTheme

private val containerShape = RoundedCornerShape(8.dp)
private val itemShape = RoundedCornerShape(6.dp)

@Composable
fun ModelTypeSwitch(
    selectedModelType: Skin.ModelType,
    onModelTypeSelected: (Skin.ModelType) -> Unit,
    modifier: Modifier = Modifier
) {
    val density = LocalDensity.current

    var containerSize by remember { mutableStateOf(IntSize.Zero) }
    val itemWidth = with(density) { (containerSize.width / 2).toDp() }
    val itemHeight = with(density) { containerSize.height.toDp() }

    val selectedIndex = if (selectedModelType == Skin.ModelType.WIDE) 0 else 1
    val indicatorOffset by animateDpAsState(
        targetValue = itemWidth * selectedIndex,
        animationSpec = tween(200)
    )

    Box(
        modifier = modifier
            .width(IntrinsicSize.Min)
            .background(
                color = AppTheme.colors.backgroundTertiary,
                shape = containerShape
            )
            .padding(4.dp)
            .onSizeChanged { containerSize = it }
    ) {
        // Animated selection indicator
        if (containerSize.width > 0) {
            Box(
                modifier = Modifier
                    .offset(x = indicatorOffset)
                    .width(itemWidth)
                    .height(itemHeight)
                    .background(
                        color = AppTheme.colors.backgroundElevatedTertiary,
                        shape = itemShape
                    )
            )
        }

        // Text items
        Row {
            ModelTypeItem(
                modifier = Modifier.weight(1f),
                text = stringResource(Res.string.skins_model_type_wide),
                onClick = { onModelTypeSelected(Skin.ModelType.WIDE) }
            )
            ModelTypeItem(
                modifier = Modifier.weight(1f),
                text = stringResource(Res.string.skins_model_type_slim),
                onClick = { onModelTypeSelected(Skin.ModelType.SLIM) },
            )
        }
    }
}

@Composable
private fun ModelTypeItem(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit,
) {
    Box(
        modifier = modifier
            .clip(itemShape)
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = AppTheme.colors.labelPrimary,
            textAlign = TextAlign.Center,
            fontSize = 12.sp
        )
    }
}
