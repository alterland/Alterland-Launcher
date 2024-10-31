package ru.alterland.launcher.ui.widgets.errors

import alterlandlauncher.composeapp.generated.resources.Res
import alterlandlauncher.composeapp.generated.resources.ic_close
import alterlandlauncher.composeapp.generated.resources.ic_error
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.painterResource
import ru.alterland.launcher.ui.theme.AppTheme

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ErrorHolder(
    modifier: Modifier = Modifier,
    errorMessage: ErrorMessage,
    onMessageClose: (String) -> Unit
) {
    val shape = RoundedCornerShape(14.dp)
    val closeShape = RoundedCornerShape(9.dp)

    val icError = painterResource(Res.drawable.ic_error)
    val icClose = painterResource(Res.drawable.ic_close)

    var isCloseButtonVisible by remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        Row(
            modifier = Modifier
                .padding(start = 5.dp, top = 5.dp)
                .sizeIn(minHeight = 64.dp, maxHeight = 128.dp)
                .fillMaxWidth()
                .background(AppTheme.colors.backgroundElevatedSecondary, shape)
                .border(BorderStroke(1.dp, AppTheme.colors.gray3), shape)
                .padding(horizontal = 10.dp)
                .onPointerEvent(PointerEventType.Enter) {
                    isCloseButtonVisible = true
                }
                .onPointerEvent(PointerEventType.Exit) {
                    isCloseButtonVisible = false
                },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                modifier = Modifier.size(32.dp),
                painter = icError,
                contentDescription = null,
            )
            Column(
                modifier = Modifier
                    .padding(start = 10.dp, top = 15.dp, bottom = 15.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    color = AppTheme.colors.labelPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    text = "Ошибка"
                )
                SelectionContainer {
                    Text(
                        modifier = Modifier.padding(top = 4.dp),
                        color = AppTheme.colors.labelPrimary,
                        fontSize = 12.sp,
                        text = errorMessage.message
                    )
                }
            }
        }

        Box(
            modifier = Modifier
                .size(18.dp)
                .onPointerEvent(PointerEventType.Enter) {
                    isCloseButtonVisible = true
                }
                .onPointerEvent(PointerEventType.Exit) {
                    isCloseButtonVisible = false
                }
        ) {
            AnimatedVisibility(
                visible = isCloseButtonVisible,
                enter = EnterTransition.None,
                exit = fadeOut()
            ) {
                Box(modifier = Modifier
                    .background(AppTheme.colors.backgroundElevatedSecondary, closeShape)
                    .clip(closeShape)
                    .border(BorderStroke(1.dp, AppTheme.colors.gray3), closeShape)
                    .clickable {
                        onMessageClose(errorMessage.id)
                    }
                    .padding(3.dp)
                ) {
                    Image(
                        painter = icClose,
                        contentDescription = "Закрыть ошибку",
                    )
                }
            }
        }
    }
}
