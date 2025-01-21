package ru.alterland.launcher.util.extentions

import kotlinx.io.files.SystemPathSeparator

infix fun String.v(subPath: String): String = "$this$SystemPathSeparator$subPath"
