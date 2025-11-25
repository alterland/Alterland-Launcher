package ru.alterland.launcher.util

import androidx.compose.ui.graphics.Color

fun Color.toAwtColor() = java.awt.Color(red, green, blue, alpha)
