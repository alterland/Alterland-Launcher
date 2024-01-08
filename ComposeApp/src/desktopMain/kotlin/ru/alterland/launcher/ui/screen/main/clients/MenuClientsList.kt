package ru.alterland.launcher.ui.screen.main.clients

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.alterland.launcher.ui.screen.main.container.MenuItem

@Composable
fun MenuClientsList(
    items: List<MenuItem> = listOf(),
    modifier: Modifier = Modifier
) {
    LazyColumn {
        items(items) { client ->
            MenuClientItem(
                item = client,
                modifier = modifier
            )
        }
    }
}
