package ru.alterland.launcher.ui.screen.main.servers

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import ru.alterland.launcher.ui.theme.AppTheme

@Composable
fun RowScope.TabItem(tab: Tab) {
    val tabNavigator = LocalTabNavigator.current
    val isSelected = tabNavigator.current == tab

    Column(
        modifier = Modifier.clickable {
            tabNavigator.current = tab
        }.padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier.padding(vertical = 8.dp),
            text = tab.options.title,
            fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal,
            color = AppTheme.colors.labelPrimary
        )
        if (isSelected) {
            Box(modifier = Modifier.size(width = 25.dp, height = 3.dp).background(AppTheme.colors.primary))
        }
    }
}
