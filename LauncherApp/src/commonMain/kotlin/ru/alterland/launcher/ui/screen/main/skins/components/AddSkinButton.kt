package ru.alterland.launcher.ui.screen.main.skins.components

import alterlandlauncher.launcherapp.generated.resources.Res
import alterlandlauncher.launcherapp.generated.resources.ic_add_circle
import alterlandlauncher.launcherapp.generated.resources.skins_add
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import ru.alterland.launcher.ui.theme.AppTheme

private val shape = RoundedCornerShape(8.dp)

@Composable
fun AddSkinButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .height(200.dp)
            .background(color = AppTheme.colors.backgroundTertiary, shape = shape)
            .clip(shape)
            .clickable(onClick = onClick)
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            painter = painterResource(Res.drawable.ic_add_circle),
            tint = AppTheme.colors.labelPrimary,
            contentDescription = null
        )
        Text(
            modifier = Modifier.padding(top = 6.dp),
            text = stringResource(Res.string.skins_add),
            color = AppTheme.colors.labelPrimary,
            textAlign = TextAlign.Center
        )
    }
}
