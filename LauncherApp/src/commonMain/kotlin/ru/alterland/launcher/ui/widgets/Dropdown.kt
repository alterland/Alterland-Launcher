package ru.alterland.launcher.ui.widgets

import alterlandlauncher.launcherapp.generated.resources.Res
import alterlandlauncher.launcherapp.generated.resources.ic_arrow_drop_down
import alterlandlauncher.launcherapp.generated.resources.ic_arrow_drop_up
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DropdownMenu
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import ru.alterland.launcher.ui.theme.AppTheme

@Composable
fun DropdownInput(
    hint: String = "",
    inputContent: (@Composable () -> Unit)?,
    dropdownContent: @Composable ColumnScope.() -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    val arrowDropDown = painterResource(Res.drawable.ic_arrow_drop_down)
    val arrowDropUp = painterResource(Res.drawable.ic_arrow_drop_up)

    Box {
        Row(
            modifier = Modifier.fillMaxWidth()
                .background(
                    color = AppTheme.colors.backgroundElevatedPrimary,
                    shape = RoundedCornerShape(8.dp)
                )
                .clip(RoundedCornerShape(8.dp))
                .clickable {
                    expanded = !expanded
                },
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            if (inputContent == null) {
                Text(text = hint)
            } else {
                inputContent()
            }
            Image(
                painter = if (expanded) arrowDropUp else arrowDropDown,
                contentDescription = null,
                modifier = Modifier
                    .padding(start = 16.dp)
                    .size(16.dp)
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            dropdownContent()
        }
    }
}

@Composable
fun DropdownItemWrapper(
    content: @Composable () -> Unit
) {
    Box(modifier = Modifier.fillMaxWidth()) {
        content()
    }
}
