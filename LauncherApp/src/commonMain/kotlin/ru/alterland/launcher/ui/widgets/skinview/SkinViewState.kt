package ru.alterland.launcher.ui.widgets.skinview

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.ImageBitmap
import coil3.ImageLoader
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.SuccessResult
import org.koin.compose.koinInject
import ru.alterland.launcher.domain.model.Skin
import ru.alterland.launcher.ui.widgets.skinview.animation.AnimationType
import ru.alterland.launcher.util.extentions.toImageBitmap

@Stable
class SkinViewState(
    initialRotationX: Float = 0f,
    initialRotationY: Float = 0f,
    initialModelType: Skin.ModelType = Skin.ModelType.WIDE,
    initialAnimation: AnimationType = AnimationType.IDLE,
    initialShowOuterLayer: Boolean = true
) {
    var rotationX by mutableFloatStateOf(initialRotationX)
        internal set

    var rotationY by mutableFloatStateOf(initialRotationY)
        internal set

    var modelType by mutableStateOf(initialModelType)

    var animationType by mutableStateOf(initialAnimation)

    var showOuterLayer by mutableStateOf(initialShowOuterLayer)

    var skinBitmap by mutableStateOf<ImageBitmap?>(null)
        internal set

    var animationSpeed by mutableFloatStateOf(1f)

    var isPaused by mutableStateOf(false)

    fun rotate(deltaX: Float, deltaY: Float) {
        rotationY += deltaX * 0.01f
        rotationX = (rotationX + deltaY * 0.01f).coerceIn(-1.2f, 1.2f)
    }

    fun resetRotation() {
        rotationX = 0f
        rotationY = 0f
    }
}

@Composable
fun rememberSkinViewState(
    skin: Skin,
    initialRotationX: Float = 0f,
    initialRotationY: Float = 0.5f,
    initialAnimation: AnimationType = AnimationType.IDLE,
    initialShowOuterLayer: Boolean = true,
    imageLoader: ImageLoader = koinInject()
): SkinViewState {
    val platformContext = LocalPlatformContext.current

    val state = remember {
        SkinViewState(
            initialRotationX = initialRotationX,
            initialRotationY = initialRotationY,
            initialModelType = skin.modelType,
            initialAnimation = initialAnimation,
            initialShowOuterLayer = initialShowOuterLayer
        )
    }

    LaunchedEffect(skin.url) {
        val request = ImageRequest.Builder(platformContext)
            .data(skin.url)
            .build()
        val result = imageLoader.execute(request)
        if (result is SuccessResult) {
            state.skinBitmap = result.image.toImageBitmap()
        }
    }

    return state
}
