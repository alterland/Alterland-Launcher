package ru.alterland.launcher.ui.widgets

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.alterland.launcher.ui.theme.AppTheme
import ru.alterland.launcher.ui.theme.defaultButtonHeight
import ru.alterland.launcher.ui.theme.defaultElementsShape

@Composable
fun Button(
    text: String = "",
    icon: Painter? = null,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
    isLoading: Boolean = false,
    backgroundGradient: Brush? = null,
    backgroundColor: Color = AppTheme.colors.primary,
    onClick: () -> Unit = {}
) {
    Row(
        if (backgroundGradient == null) {
            modifier
                .background(backgroundColor, defaultElementsShape)
                .defaultMinSize(minHeight = defaultButtonHeight)
                .clickable(
                    enabled = !isLoading,
                    onClick = onClick
                )
        } else {
            modifier
                .background(backgroundGradient, defaultElementsShape)
                .fillMaxWidth()
                .defaultMinSize(minHeight = defaultButtonHeight)
                .clickable(
                    enabled = !isLoading,
                    onClick = onClick
                )
        },
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (icon == null) {
            Text(
                text = text,
                fontSize = 13.sp,
                color = AppTheme.colors.forceWhitePrimary,
                modifier = Modifier.padding(start = 20.dp)
            )
        } else {
            Image(
                painter = icon,
                contentDescription = null,
                modifier = Modifier.padding(10.dp)
            )
        }
        Column(
            modifier = Modifier.width(22.dp)
        ) {
            if (isLoading)
                AppleCircularProgressIndicator(
                    color =  AppTheme.colors.forceWhitePrimary,
                    strokeWidth = 1.dp,
                    modifier = Modifier.padding(start = 8.dp).size(13.dp)
                )
        }
    }
}

//@Composable
//@Preview
//private fun ButtonPreview() {
//    Button()
//}