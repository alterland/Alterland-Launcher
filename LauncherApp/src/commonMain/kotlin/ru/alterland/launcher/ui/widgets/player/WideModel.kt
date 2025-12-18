package ru.alterland.launcher.ui.widgets.player

import ru.alterland.launcher.ui.widgets.player.elements.Box3D

private const val OVERLAY_SCALE = 0.5f

val wideModel = listOf(
    // === Base layers ===
    // Head: UV (0, 0)
    Box3D(-4f, 24f, -4f, 8f, 8f, 8f, uvX = 0, uvY = 0),
    // Body: UV (16, 16)
    Box3D(-4f, 12f, -2f, 8f, 12f, 4f, uvX = 16, uvY = 16),
    // Right Arm: UV (40, 16)
    Box3D(-8f, 12f, -2f, 4f, 12f, 4f, uvX = 40, uvY = 16),
    // Left Arm: UV (32, 48)
    Box3D(4f, 12f, -2f, 4f, 12f, 4f, uvX = 32, uvY = 48),
    // Right Leg: UV (0, 16)
    Box3D(-3.9f, 0f, -2f, 4f, 12f, 4f, uvX = 0, uvY = 16),
    // Left Leg: UV (16, 48)
    Box3D(-0.1f, 0f, -2f, 4f, 12f, 4f, uvX = 16, uvY = 48),

    // === Overlay layers ===
    // Head Layer 2 (Helm): UV (32, 0), size 8x8x8
    Box3D(
        -4f - OVERLAY_SCALE,
        24f - OVERLAY_SCALE,
        -4f - OVERLAY_SCALE,
        8f + OVERLAY_SCALE * 2,
        8f + OVERLAY_SCALE * 2,
        8f + OVERLAY_SCALE * 2,
        uvX = 32,
        uvY = 0,
        uvW = 8,
        uvH = 8,
        uvD = 8
    ),
    // Torso Layer 2: UV (16, 32), size 8x12x4
    Box3D(
        -4f - OVERLAY_SCALE,
        12f - OVERLAY_SCALE,
        -2f - OVERLAY_SCALE,
        8f + OVERLAY_SCALE * 2,
        12f + OVERLAY_SCALE * 2,
        4f + OVERLAY_SCALE * 2,
        uvX = 16,
        uvY = 32,
        uvW = 8,
        uvH = 12,
        uvD = 4
    ),
    // Right Arm Layer 2: UV (40, 32), size 4x12x4
    Box3D(
        -8f - OVERLAY_SCALE,
        12f - OVERLAY_SCALE,
        -2f - OVERLAY_SCALE,
        4f + OVERLAY_SCALE * 2,
        12f + OVERLAY_SCALE * 2,
        4f + OVERLAY_SCALE * 2,
        uvX = 40,
        uvY = 32,
        uvW = 4,
        uvH = 12,
        uvD = 4
    ),
    // Left Arm Layer 2: UV (48, 48), size 4x12x4
    Box3D(
        4f - OVERLAY_SCALE,
        12f - OVERLAY_SCALE,
        -2f - OVERLAY_SCALE,
        4f + OVERLAY_SCALE * 2,
        12f + OVERLAY_SCALE * 2,
        4f + OVERLAY_SCALE * 2,
        uvX = 48,
        uvY = 48,
        uvW = 4,
        uvH = 12,
        uvD = 4
    ),
    // Right Leg Layer 2: UV (0, 32), size 4x12x4
    Box3D(
        -3.9f - OVERLAY_SCALE,
        0f - OVERLAY_SCALE,
        -2f - OVERLAY_SCALE,
        4f + OVERLAY_SCALE * 2,
        12f + OVERLAY_SCALE * 2,
        4f + OVERLAY_SCALE * 2,
        uvX = 0,
        uvY = 32,
        uvW = 4,
        uvH = 12,
        uvD = 4
    ),
    // Left Leg Layer 2: UV (0, 48), size 4x12x4
    Box3D(
        -0.1f - OVERLAY_SCALE,
        0f - OVERLAY_SCALE,
        -2f - OVERLAY_SCALE,
        4f + OVERLAY_SCALE * 2,
        12f + OVERLAY_SCALE * 2,
        4f + OVERLAY_SCALE * 2,
        uvX = 0,
        uvY = 48,
        uvW = 4,
        uvH = 12,
        uvD = 4
    )
)
