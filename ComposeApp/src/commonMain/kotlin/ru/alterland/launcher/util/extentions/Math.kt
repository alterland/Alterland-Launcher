package ru.alterland.launcher.util.extentions

fun Long.bytesToMegabytes(): Long = this.shr(20)
fun Long.bytesToMegabytesString(): String = String.format("%.1f", bytesToMegabytes())
