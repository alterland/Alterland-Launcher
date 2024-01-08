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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun LoadableImage(
    height: Dp = 100.dp,
    width: Dp = 100.dp,
    size: Dp? = null,
    shape: RoundedCornerShape = RoundedCornerShape(width/2)
) {
    var heightM = height
    var widthM = width
    var shapeM = shape

    if (size != null) {
        heightM = size
        widthM = size
        shapeM =  RoundedCornerShape(size/2)
    }

    //https://developer.android.com/reference/kotlin/androidx/compose/animation/core/InfiniteTransition
    val infiniteTransition = rememberInfiniteTransition()

    val transparencyDec by infiniteTransition.animateFloat(
        initialValue = 30F,
        targetValue = 80F,
        animationSpec = infiniteRepeatable(
            animation = tween(500),
            repeatMode = RepeatMode.Reverse
        )
    )

    val hexAlpha = "0x"+Integer.toHexString(transparencyDec.toInt())
    val color = Color(255,255,255, Integer.decode(hexAlpha))

    Box(modifier = Modifier
        .width(widthM)
        .height(heightM)
        .background(color, shapeM)
    )
}