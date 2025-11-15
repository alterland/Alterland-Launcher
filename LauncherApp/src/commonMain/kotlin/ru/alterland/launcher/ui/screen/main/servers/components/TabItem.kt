package ru.alterland.launcher.ui.screen.main.servers.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ru.alterland.launcher.ui.theme.AppTheme

@Composable
fun TabItem(
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .clickable(!isSelected) { onClick() }
                .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier.padding(vertical = 8.dp),
            text = title,
            fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal,
            color = AppTheme.colors.labelPrimary
        )
        if (isSelected) {
            Box(modifier = Modifier.size(width = 25.dp, height = 3.dp).background(AppTheme.colors.primary))
        }
    }
}
