package ru.alterland.launcher.ui.screen.main.skins.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import ru.alterland.launcher.ui.theme.AppTheme

private val shape = RoundedCornerShape(4.dp)

@Composable
fun AnimationButton(
    painter: Painter,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor = if (isSelected) {
        AppTheme.colors.backgroundElevatedSecondary
    } else {
        AppTheme.colors.backgroundElevatedTertiary
    }

    Box(
        modifier = modifier
            .size(26.dp)
            .background(color = backgroundColor, shape = shape)
            .clip(shape)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            modifier = Modifier.size(24.dp),
            painter = painter,
            contentDescription = null,
            tint = AppTheme.colors.labelSecondary
        )
    }
}
