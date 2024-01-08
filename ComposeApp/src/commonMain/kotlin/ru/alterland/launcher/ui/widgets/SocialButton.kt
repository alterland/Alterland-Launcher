package ru.alterland.launcher.ui.widgets

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.alterland.launcher.ui.theme.AppTheme
import ru.alterland.launcher.ui.theme.defaultElementsShape

@Composable
fun SocialButton(
    text: String,
    icon: Painter,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    isEnabled: Boolean = true,
    onClick: () -> Unit = {}
) {

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(AppTheme.colors.gray5, defaultElementsShape)
            .clickable(enabled = isEnabled, onClick = onClick),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            icon,
            null,
            modifier = Modifier.padding(start = 40.dp)
        )
        Text(
            text = text,
            fontSize = 13.sp,
            color = AppTheme.colors.labelPrimary,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 12.dp)
        )
        if (isLoading)
            AppleCircularProgressIndicator(
                color =  AppTheme.colors.labelPrimary,
                strokeWidth = 2.dp,
                modifier = Modifier.size(13.dp)
            )
    }
}