package ru.alterland.launcher.ui.widgets

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.alterland.launcher.ui.theme.AppTheme

@Composable
fun RadioButton(
    modifier: Modifier = Modifier,
    selected: Boolean = false,
    text: String = "",
    onClick: () -> Unit = {}
) {
    val size = 14

    Row(
        modifier = modifier.clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(size.dp)
                .border(BorderStroke(1.5.dp, if (selected) AppTheme.colors.primary else AppTheme.colors.labelSecondary), CircleShape)
        ) {
            if (selected) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .background(AppTheme.colors.primary, CircleShape)
                )
            }
        }
        Text(
            text = text,
            fontSize = 12.sp,
            color = AppTheme.colors.labelSecondary,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}
