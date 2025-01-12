package ru.alterland.launcher.util

import kotlinx.io.files.SystemPathSeparator

infix fun String.v(subPath: String): String = "$this$SystemPathSeparator$subPath"
