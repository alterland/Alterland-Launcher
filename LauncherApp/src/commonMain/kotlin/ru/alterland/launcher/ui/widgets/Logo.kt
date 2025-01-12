package ru.alterland.launcher.ui.widgets

import alterlandlauncher.launcherapp.generated.resources.Res
import alterlandlauncher.launcherapp.generated.resources.logo_house
import alterlandlauncher.launcherapp.generated.resources.logo_name
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import ru.alterland.launcher.ui.theme.AppTheme

@Composable
fun Logo(
    textOnly: Boolean = false,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
    ) {
        if (!textOnly) {
            Image(
                painter = painterResource(Res.drawable.logo_house),
                contentDescription = null,
                Modifier.height(53.dp)
            )
            Box(
                modifier = Modifier
                    .size(width = 1.dp, height = 44.dp)
                    .padding(start = 18.dp, top = 3.dp, end = 17.dp)
            )
        }
        Image(
            painter = painterResource(Res.drawable.logo_name),
            contentDescription = null,
            modifier = Modifier.padding(top = 4.dp),
            colorFilter = ColorFilter.tint(AppTheme.colors.labelPrimary)
        )
    }
}

@Composable
@Preview
private fun LogoPreview() {
    Logo(true)
}
