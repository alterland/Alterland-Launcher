package ru.alterland.launcher.ui.screen.main.container.components

import alterlandlauncher.launcherapp.generated.resources.Res
import alterlandlauncher.launcherapp.generated.resources.ic_exit
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.ImageLoader
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.SuccessResult
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.koinInject
import ru.alterland.launcher.domain.model.Role
import ru.alterland.launcher.domain.model.User
import ru.alterland.launcher.ui.theme.AppTheme
import ru.alterland.launcher.ui.widgets.LoadableItem
import ru.alterland.launcher.ui.widgets.loadables.LoadableImage
import ru.alterland.launcher.ui.widgets.loadables.LoadableText
import ru.alterland.launcher.util.base.Resource
import ru.alterland.launcher.util.extentions.extractSkinFace
import ru.alterland.launcher.util.extentions.toImageBitmap

@Composable
fun rememberSkinFace(
    skinUrl: String?,
    imageLoader: ImageLoader = koinInject()
): ImageBitmap? {
    val platformContext = LocalPlatformContext.current
    var faceBitmap by remember { mutableStateOf<ImageBitmap?>(null) }

    LaunchedEffect(skinUrl) {
        if (skinUrl == null) {
            faceBitmap = null
            return@LaunchedEffect
        }
        val request = ImageRequest.Builder(platformContext)
            .data(skinUrl)
            .build()
        val result = imageLoader.execute(request)
        if (result is SuccessResult) {
            faceBitmap = result.image.toImageBitmap().extractSkinFace()
        }
    }

    return faceBitmap
}

private val imageShape = RoundedCornerShape(5.dp)

@Composable
fun MiniProfile(
    modifier: Modifier = Modifier,
    user: Resource<User>?,
    onExit: () -> Unit = {}
) {
    val skinUrl = user?.getOrNull()?.skin?.url
    val faceBitmap = rememberSkinFace(skinUrl)

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier.fillMaxWidth()
    ) {
        Row {
            LoadableItem(
                isLoading = user is Resource.Loading,
                placeholder = {
                    LoadableImage(modifier = Modifier.clip(imageShape).size(32.dp))
                }
            ) {
                if (faceBitmap != null) {
                    Image(
                        bitmap = faceBitmap,
                        contentDescription = null,
                        filterQuality = FilterQuality.None,
                        modifier = Modifier.clip(imageShape).size(32.dp)
                    )
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
                    user?.getOrNull()?.let {
                        Text(
                            text = it.nickname,
                            fontSize = 13.sp,
                            color = AppTheme.colors.labelPrimary
                        )
                        Text(
                            text = it.role?.name.orEmpty(),
                            fontSize = 10.sp,
                            color = AppTheme.colors.labelSecondary,
                            modifier = Modifier.padding(top = 2.dp)
                        )
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

@Preview
@Composable
private fun MiniProfilePreview() {
    MiniProfile(
        user = Resource.Content(User(
            id = "",
            email = "",
            nickname = "Aviator737",
            realName = "",
            role = Role.ADMIN,
            skin = null
        ))
    )
}
