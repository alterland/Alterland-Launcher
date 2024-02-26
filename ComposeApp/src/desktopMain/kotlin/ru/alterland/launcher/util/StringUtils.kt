package ru.alterland.launcher.util

import java.nio.file.FileSystems

private val SEPARATOR = FileSystems.getDefault().separator

infix fun String.v(subPath: String): String = "$this$SEPARATOR$subPath"
