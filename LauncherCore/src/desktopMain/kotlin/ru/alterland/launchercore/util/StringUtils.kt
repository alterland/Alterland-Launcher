package ru.alterland.launchercore.util

private val SEPARATOR = System.getProperty("file.separator")

infix fun String.`V`(subPath: String): String = "$this$SEPARATOR$subPath"
