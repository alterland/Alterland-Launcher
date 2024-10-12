package ru.alterland.launcher.util.extentions

fun Long.bytesToMegabytes() = String.format("%.1f", this / 1048576.0)
