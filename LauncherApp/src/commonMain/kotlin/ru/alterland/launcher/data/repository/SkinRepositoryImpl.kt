package ru.alterland.launcher.data.repository

import com.ubivashka.minecraft.skin.renderer.SkinRenderer
import kotlinx.coroutines.CoroutineDispatcher
import ru.alterland.launcher.domain.model.Skin
import ru.alterland.launcher.domain.repository.SkinRepository
import kotlinx.coroutines.withContext

import com.ubivashka.minecraft.skin.renderer.part.*
import java.awt.Image
import java.awt.image.BufferedImage
import javax.imageio.ImageIO
import kotlin.io.path.Path
import kotlin.io.path.inputStream


class SkinRepositoryImpl(
    private val dispatcherIo: CoroutineDispatcher
): SkinRepository {

    private var skins = listOf<Skin>(
//        Skin(name = "Steve", imageUrl = "skin1.png"),
//        Skin(name = "Alex", imageUrl = "skin1.png"),
//        Skin(name = "Zuri", imageUrl = "skin1.png"),
//        Skin(name = "Sunny", imageUrl = "skin1.png"),
//        Skin(name = "Noor", imageUrl = "skin1.png"),
    )

    override suspend fun getSkins(): List<Skin> = withContext(dispatcherIo){
        skins.toList()
    }

    override suspend fun addSkin(path: String) = withContext(dispatcherIo){
        val image = loadImage(path)
        val newSkin = Skin(name = "steve", image = image)

        skins = listOf(newSkin) + skins
    }

    override suspend fun deleteSkin(skin: Skin) = withContext(dispatcherIo) {
        skins = skins.filter { it != skin }
    }

    private fun loadImage(path: String): BufferedImage {
        val image = Path(path)
        val inputImage = ImageIO.read(image.inputStream())
        val skinPartImage = SkinRenderer.frontOfSkin(inputImage)

        return SkinRenderer.resize(
            skinPartImage,
            skinPartImage.width * 16,
            skinPartImage.height * 16,
            Image.SCALE_REPLICATE
        )
    }
}