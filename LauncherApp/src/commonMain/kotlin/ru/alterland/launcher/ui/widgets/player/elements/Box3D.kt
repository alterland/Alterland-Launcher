package ru.alterland.launcher.ui.widgets.player.elements

data class Box3D(
    val originX: Float,
    val originY: Float,
    val originZ: Float,
    val sizeX: Float,
    val sizeY: Float,
    val sizeZ: Float,
    val uvX: Int = 0,
    val uvY: Int = 0,
    // UV dimensions (for overlay layers that have different visual size than UV size)
    val uvW: Int? = null,
    val uvH: Int? = null,
    val uvD: Int? = null
)
