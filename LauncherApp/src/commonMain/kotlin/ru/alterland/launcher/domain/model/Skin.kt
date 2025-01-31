package ru.alterland.launcher.domain.model

import java.awt.image.BufferedImage


data class Skin(val id: String = generateSkinID(), val name: String, val image: BufferedImage) {
    companion object {
        fun generateSkinID(): String = System.currentTimeMillis().toString()
    }
}

