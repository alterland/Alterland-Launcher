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

    private var skins = listOf<Skin>()

    override fun getSkins(): List<Skin> = skins

    override suspend fun addSkin(path: String) = withContext(dispatcherIo){
        val image = loadImage(path)
        val newSkin = Skin(
            name = Path(path).fileName.toString().substringBeforeLast(".").take(10),
            image = image
        )
//        skins.add(0, newSkin)
        skins = listOf(newSkin) + skins
    }

    override suspend fun deleteSkin(skin: Skin): Unit = withContext(dispatcherIo) {
        skins = skins.toMutableList().filter { it.id != skin.id }
    }

    override suspend fun renameSkin(skin: Skin, newName: String) = withContext(dispatcherIo) {
        val mutableSkins = skins.toMutableList()
        val index = mutableSkins.indexOfFirst { it.id == skin.id }
        if (index != -1) {
            mutableSkins[index] = mutableSkins[index].copy(name = newName)
        }
        skins = mutableSkins
    }

    private fun loadImage(path: String): BufferedImage {
        val image = Path(path)
        val inputImage = ImageIO.read(image.inputStream())
        val skinPartImage = SkinRenderer.frontOfSkin(inputImage)

        val resizedImage = SkinRenderer.resize(
            skinPartImage,
            skinPartImage.width * 16,
            skinPartImage.height * 16,
            Image.SCALE_REPLICATE
        )
        return makeTransparent(resizedImage)
    }

    private fun makeTransparent(image: BufferedImage): BufferedImage {
        val width = image.width
        val height = image.height
        val transparentImage = BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)

        for (x in 0 until width) {
            for (y in 0 until height) {
                val pixel = image.getRGB(x, y)
                if (pixel == 0xFF000000.toInt()) {
                    transparentImage.setRGB(x, y, 0x00000000)
                } else {
                    transparentImage.setRGB(x, y, pixel)
                }
            }
        }
        return transparentImage
    }
}