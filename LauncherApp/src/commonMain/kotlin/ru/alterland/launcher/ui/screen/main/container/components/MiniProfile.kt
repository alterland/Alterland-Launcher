package ru.alterland.launcher.ui.screen.main.container.components

import alterlandlauncher.launcherapp.generated.resources.Res
import alterlandlauncher.launcherapp.generated.resources.avatar_temp
import alterlandlauncher.launcherapp.generated.resources.ic_exit
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import ru.alterland.launcher.domain.model.User
import ru.alterland.launcher.ui.theme.AppTheme
import ru.alterland.launcher.ui.widgets.LoadableItem
import ru.alterland.launcher.ui.widgets.loadables.LoadableImage
import ru.alterland.launcher.ui.widgets.loadables.LoadableText
import ru.alterland.launcher.util.base.Resource

@Composable
fun MiniProfile(
    user: Resource<User>?,
    avatar: Painter = painterResource(Res.drawable.avatar_temp),
    modifier: Modifier = Modifier,
    onExit: () -> Unit = {}
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier.fillMaxWidth()
    ) {
        Row {
            LoadableItem(
                isLoading = user is Resource.Loading || user == null,
                placeholder = {
                    LoadableImage(size = 32.dp)
                }
            ) {
                when(user) {
                    is Resource.Content -> {
                        Image(
                            painter = avatar,
                            contentScale = ContentScale.Crop,
                            contentDescription = null,
                            modifier = Modifier.clip(CircleShape).size(32.dp)
                        )
                    }
                    is Resource.Error -> {

                    }
                    else -> {}
                }
            }
            Column (
                modifier = Modifier.padding(start = 8.dp)
            ) {
                LoadableItem(
                    isLoading = user is Resource.Loading || user == null,
                    placeholder = {
                        LoadableText()
                        LoadableText(11.sp, 50.dp, Modifier.padding(top = 2.dp))
                    }
                ) {
                    when(user) {
                        is Resource.Content -> {
                            Text(
                                text = user.data.nickname,
                                fontSize = 13.sp,
                                color = AppTheme.colors.labelPrimary
                            )
                            Text(
                                text = user.data.role?.name.orEmpty(),
                                fontSize = 10.sp,
                                color = AppTheme.colors.labelSecondary,
                                modifier = Modifier.padding(top = 2.dp)
                            )
                        }
                        is Resource.Error -> {

                        }
                        else -> {}
                    }
                }
            }
        }
        Image(
            painter = painterResource(Res.drawable.ic_exit),
            contentDescription = null,
            modifier = Modifier.padding(start = 8.dp).size(20.dp).clickable { onExit() }
        )
    }
}

@Composable
@Preview
private fun MiniProfilePreview() {
    MiniProfile(user = Resource.Content(User(
        id = "",
        email = "",
        nickname = "Aviator737",
        realName = "",
        role = User.Role(
            id = "",
            name = "Игрок",
            strength = 1
        )
    )))
}
