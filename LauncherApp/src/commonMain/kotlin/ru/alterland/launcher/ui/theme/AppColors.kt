package ru.alterland.launcher.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

//https://developer.apple.com/design/human-interface-guidelines/foundations/color/

@Immutable
class AppColors(
    val primary: Color,

    //default
    val red: Color,
    val orange: Color,
    val yellow: Color,
    val green: Color,
    val mint: Color,
    val teal: Color,
    val cyan: Color,
    val blue: Color,
    val indigo: Color,
    val purple: Color,
    val pink: Color,
    val brown: Color,

    val gray: Color,
    val gray2: Color,
    val gray3: Color,
    val gray4: Color,
    val gray5: Color,
    val gray6: Color,

    val forceWhitePrimary: Color,
    val forceWhiteSecondary: Color,
    val forceWhiteTertiary: Color,

    val forceDarkPrimary: Color,
    val forceDarkSecondary: Color,
    val forceDarkTertiary: Color,
    val forceDarkQuaternary: Color,

    //background
    val backgroundPrimary: Color,
    val backgroundSecondary: Color,
    val backgroundTertiary: Color,
    val backgroundElevatedPrimary: Color,
    val backgroundElevatedSecondary: Color,
    val backgroundElevatedTertiary: Color,

    //label
    val labelPrimary: Color,
    val labelSecondary: Color,
    val labelTertiary: Color,
    val labelQuaternary: Color,

    val transparent: Color,

    val isDark: Boolean
)
