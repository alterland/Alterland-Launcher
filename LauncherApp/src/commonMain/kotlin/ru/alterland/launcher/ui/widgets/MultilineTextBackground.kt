package ru.alterland.launcher.ui.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight.Companion.W400
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import ru.alterland.launcher.ui.theme.AppTheme

@Composable
fun MultilineTextBackground(
    text: String,
    color: Color = AppTheme.colors.labelPrimary,
    backgroundColor: Color = AppTheme.colors.primary,
    fontSize: TextUnit = TextUnit.Unspecified,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        text.split("\n").forEach { textLine ->
            Text(
                text = textLine,
                fontWeight = W400,
                modifier = Modifier
                    .background(backgroundColor)
                    .padding(3.dp),
                color = color,
                fontSize = fontSize
            )
        }
    }
}
