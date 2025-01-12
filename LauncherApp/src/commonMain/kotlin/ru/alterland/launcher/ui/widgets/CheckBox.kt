package ru.alterland.launcher.ui.widgets

import alterlandlauncher.launcherapp.generated.resources.Res
import alterlandlauncher.launcherapp.generated.resources.ic_checkbox_check
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.painterResource
import ru.alterland.launcher.ui.theme.AppTheme

@Composable
fun CheckBox(
    checked: Boolean = false,
    text: String = "",
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    val icCheck = painterResource(Res.drawable.ic_checkbox_check)
    val size = 14
    val shape = RoundedCornerShape((size/2).dp)

    Row(modifier = modifier.clickable(onClick = onClick)) {
        if (checked) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(size.dp)
                    .background(AppTheme.colors.primary, shape)
            ) {
                Image(
                    painter = icCheck,
                    contentDescription = null,
                    modifier = Modifier.size(7.dp)
                )
            }
        } else {
            Box(
                modifier = Modifier
                    .size(size.dp)
                    .border(BorderStroke(1.5.dp, AppTheme.colors.labelSecondary), shape)
            ) {}
        }
        Text(
            text = text,
            fontSize = 12.sp,
            color = AppTheme.colors.labelSecondary,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}
