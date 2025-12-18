package ru.alterland.launcher.ui.widgets.loadables

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.alterland.launcher.ui.theme.AppTheme

@Composable
fun LoadableText(
    height: TextUnit = 14.sp,
    width: Dp = 100.dp,
    modifier: Modifier = Modifier
) {
    val heightDp: Dp = with(LocalDensity.current) { height.toDp() }
    val round = heightDp/3

    val infiniteTransition = rememberInfiniteTransition()

    val alpha by infiniteTransition.animateFloat(
        initialValue = 30F,
        targetValue = 80F,
        animationSpec = infiniteRepeatable(
            animation = tween(500),
            repeatMode = RepeatMode.Reverse
        )
    )

    Box(modifier = modifier
        .width(width)
        .height(heightDp)
        .background(
            AppTheme.colors.forceWhitePrimary.copy(alpha = alpha),
            RoundedCornerShape(round)
        )
    )
}