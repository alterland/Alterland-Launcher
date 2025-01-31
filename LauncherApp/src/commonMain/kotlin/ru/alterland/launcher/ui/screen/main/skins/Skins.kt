package ru.alterland.launcher.ui.screen.main.skins

import alterlandlauncher.launcherapp.generated.resources.Res
import alterlandlauncher.launcherapp.generated.resources.ic_add_circle
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toPainter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import io.github.vinceglb.filekit.compose.rememberFilePickerLauncher
import io.github.vinceglb.filekit.core.PickerMode
import io.github.vinceglb.filekit.core.PickerType
import io.ktor.http.content.*
import org.jetbrains.compose.resources.painterResource
import ru.alterland.launcher.ui.theme.AppTheme
import ru.alterland.launcher.ui.widgets.Button
import ru.alterland.launcher.ui.widgets.Input


@Composable
fun Skins(
    state: SkinsContract.State,
    onEvent: (e: SkinsContract.Event) -> Unit,
) {
    val launcher = rememberFilePickerLauncher(
        type = PickerType.Image,
        mode = PickerMode.Single,
        title = "Выберите скин"
    ) { directory -> directory?.path?.let { onEvent(SkinsContract.Event.AddSkin(path = it)) } }

    state.renamingSkin?.let { renamingSkin ->
        Dialog(
            onDismissRequest = { onEvent(SkinsContract.Event.RenameSkin(null))},
            properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true)
        ) {
            Column(
                modifier = Modifier
                    .background(Color.Transparent, RoundedCornerShape(12.dp))
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Введите новое имя",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(8.dp))
                Input(
                    text = state.newName,
                    singleLine = true,
//                    onInput = { onEvent(SkinsContract.Event.UpdateNewName(it))},
                    onInput = {
                        if (it.length <= 10) {
                            onEvent(SkinsContract.Event.UpdateNewName(it))
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        text = "Сохранить",
                        onClick = { onEvent(SkinsContract.Event.FinishRename(renamingSkin, state.newName))},
                        backgroundColor = Color.Gray
                    )
                    Button(
                        text = "Отмена",
                        onClick = {onEvent(SkinsContract.Event.RenameSkin(null))},
                        backgroundColor = Color.Gray
                    )
                }
            }
        }
    }

    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.colors.backgroundTertiary)
            .padding(12.dp)
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Текущий",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Box(
                modifier = Modifier
                    .size(250.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color.Transparent),
                contentAlignment = Alignment.Center
            ) {
                state.currentSkin?.let { skin ->
                    Image(
                        painter = skin.image.toPainter(),
                        contentDescription = null,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.size(250.dp)
                    )
                }
            }
        }

        Divider(color = Color.White, modifier = Modifier
            .fillMaxHeight()
            .width(2.dp))

        Column(
            modifier = Modifier
                .weight(2f)
                .fillMaxHeight()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Библиотека",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            LazyVerticalGrid(
                columns = GridCells.Adaptive(100.dp),
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Box(
                        contentAlignment = Alignment.Center
                    ) {
                        Button(
                            text = "Новый скин",
                            onClick = { launcher.launch() },
                            modifier = Modifier
                                .size(150.dp)
                                .padding(top = 60.dp),
                            backgroundColor = Color.Transparent
                        )
                        Image(
                            painter = painterResource(Res.drawable.ic_add_circle),
                            contentDescription = null,
                            modifier = Modifier
                                .size(40.dp)
                        )
                    }
                }

                items(state.skinLibrary) { skin ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier
                            .width(200.dp)
                            .wrapContentHeight()
                    ) {
                        Text(
                            text = skin.name,
                            color = Color.White,
                            fontSize = 16.sp,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                        Box(
                            modifier = Modifier
                                .size(200.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color.Transparent)
                                .pointerInput(Unit) {
                                    detectTapGestures(
                                        onTap = { onEvent(SkinsContract.Event.ToggleHover(skin))}
                                    )
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Box {
                                Image(
                                    painter = skin.image.toPainter(),
                                    contentDescription = null,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .size(200.dp)
                                        .align(Alignment.Center)
                                )

                                println(state.hoveredSkin)
                                if (state.hoveredSkin == skin) {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(5.dp)
                                            .align(Alignment.Center),
                                        verticalArrangement = Arrangement.spacedBy(6.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                    ) {
                                        val buttonModifier = Modifier
                                            .fillMaxWidth(1f)
                                            .height(25.dp)

                                        Button(
                                            text = "Применить",
                                            onClick = { onEvent(SkinsContract.Event.ApplySkin(skin)) },
                                            modifier = buttonModifier,
                                            backgroundColor = Color.Gray,
                                        )
                                        Button(
                                            text = "Переименовать",
                                            onClick = { onEvent(SkinsContract.Event.RenameSkin(skin)) },
                                            modifier = buttonModifier,
                                            backgroundColor = Color.Gray
                                        )
                                        Button(
                                            text = "Удалить",
                                            onClick = { onEvent(SkinsContract.Event.DeleteSkin(skin)) },
                                            modifier = buttonModifier,
                                            backgroundColor = Color.Gray
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}