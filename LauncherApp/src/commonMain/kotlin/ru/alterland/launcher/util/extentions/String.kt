package ru.alterland.launcher.util.extentions

import kotlinx.io.files.SystemPathSeparator

val pathSeparator = SystemPathSeparator.toString()

infix fun String.v(subPath: String): String = "$this$pathSeparator$subPath"
