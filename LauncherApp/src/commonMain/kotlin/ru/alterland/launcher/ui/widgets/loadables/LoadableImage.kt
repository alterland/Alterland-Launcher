package ru.alterland.launcher.ui.widgets.loadables

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import ru.alterland.launcher.ui.theme.AppTheme

@Composable
fun LoadableImage(
    modifier: Modifier = Modifier,
    shape: RoundedCornerShape = RoundedCornerShape(50)
) {
    //https://developer.android.com/reference/kotlin/androidx/compose/animation/core/InfiniteTransition
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
        .background(AppTheme.colors.forceWhitePrimary.copy(alpha = alpha), shape)
    )
}
