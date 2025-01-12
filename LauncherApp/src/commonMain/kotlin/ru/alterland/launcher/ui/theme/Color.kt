package ru.alterland.launcher.ui.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

fun lightColors(
    red: Color = Color(255, 59, 48),
    orange: Color = Color(255, 149, 0),
    yellow: Color = Color(255, 204 ,0),
    green: Color = Color(52,199,89),
    mint: Color = Color(0,199,190),
    teal: Color = Color(48,176,199),
    cyan: Color = Color(50,173,230),
    blue: Color = Color(0,122,255),
    indigo: Color = Color(88,86,214),
    purple: Color = Color(175,82,222),
    pink: Color = Color(255,45,85),
    brown: Color = Color(162,132,94),

    gray: Color = Color(142,142,147),
    gray2: Color = Color(174,174,178),
    gray3: Color = Color(199,199,204),
    gray4: Color = Color(209,209,214),
    gray5: Color = Color(229,229,234),
    gray6: Color = Color(242,242,247),

    forceWhitePrimary: Color = Color(255,255,255),
    forceWhiteSecondary: Color = Color(235,235,245,0x99),
    forceWhiteTertiary: Color = Color(235,235,245,0x4D),
    forceWhiteQuaternary: Color = Color(235,235,245,0x2E),

    backgroundPrimary: Color = Color(255,255,255),
    backgroundSecondary: Color = Color(242,242,247),
    backgroundTertiary: Color = Color(255,255,255),
    backgroundElevatedSecondary: Color = Color(255,255,255, 0x4D),
    backgroundElevatedTertiary: Color = Color(255,255,255, 0x2E),

    labelPrimary: Color = Color(0,0,0),
    labelSecondary: Color = Color(60,60,67, 0x99),
    labelTertiary: Color = Color(60,60,67, 0x4D),
    labelQuaternary: Color = Color(60,60,67, 0x2E),
) = AppColors(
    primary = blue,

    red = red,
    orange = orange,
    yellow = yellow,
    green = green,
    mint = mint,
    teal = teal,
    cyan = cyan,
    blue = blue,
    indigo = indigo,
    purple = purple,
    pink = pink,
    brown = brown,

    gray = gray,
    gray2 = gray2,
    gray3 = gray3,
    gray4 = gray4,
    gray5 = gray5,
    gray6 = gray6,

    forceWhitePrimary = forceWhitePrimary,
    forceWhiteSecondary = forceWhitePrimary,
    forceWhiteTertiary = forceWhiteTertiary,

    forceDarkPrimary = labelPrimary,
    forceDarkSecondary = labelSecondary,
    forceDarkTertiary = labelTertiary,
    forceDarkQuaternary = labelQuaternary,

    backgroundPrimary = backgroundPrimary,
    backgroundSecondary = backgroundSecondary,
    backgroundTertiary =  backgroundTertiary,
    backgroundElevatedPrimary = forceWhitePrimary,
    backgroundElevatedSecondary = backgroundElevatedSecondary,
    backgroundElevatedTertiary = backgroundElevatedTertiary,

    labelPrimary = labelPrimary,
    labelSecondary = labelSecondary,
    labelTertiary = labelTertiary,
    labelQuaternary = labelQuaternary,

    transparent = Color(0x00000000),
    isDark = false
)

fun darkColors(
    red: Color = Color(255,69,58),
    orange: Color = Color(255,159,10),
    yellow: Color = Color(255,214,10),
    green: Color = Color(50,215,75),
    mint: Color = Color(102,212,207),
    teal: Color = Color(106,196,220),
    cyan: Color = Color(90,200,245),
    blue: Color = Color(10,132,255),
    indigo: Color = Color(94,92,230),
    purple: Color = Color(191,90,242),
    pink: Color = Color(255,55,95),
    brown: Color = Color(172,142,104),

    gray: Color = Color(142,142,147),
    gray2: Color = Color(99,99,102),
    gray3: Color = Color(72,72,74),
    gray4: Color = Color(58,58,60),
    gray5: Color = Color(44,44,46),
    gray6: Color = Color(28,28,30),

    forceWhitePrimary: Color = Color(255,255,255),
    forceWhiteSecondary: Color = Color(235,235,245,0x99),
    forceWhiteTertiary: Color = Color(235,235,245,0x4D),
    forceWhiteQuaternary: Color = Color(235,235,245,0x2E),

    forceDarkPrimary: Color = Color(0,0,0),
    forceDarkSecondary: Color = Color(60,60,67, 0x99),
    forceDarkTertiary: Color = Color(60,60,67, 0x4D),
    forceDarkQuaternary: Color = Color(60,60,67, 0x2E),

    backgroundPrimary: Color = Color(0,0,0),
    backgroundSecondary: Color = Color(28,28,30),
    backgroundTertiary: Color = Color(44,44,46),
    backgroundElevatedPrimary: Color = Color(28,28,30),
    backgroundElevatedSecondary: Color = Color(44,44,46),
    backgroundElevatedTertiary: Color = Color(58,58,60),
) = AppColors(
    primary = blue,

    red = red,
    orange = orange,
    yellow = yellow,
    green = green,
    mint = mint,
    teal = teal,
    cyan = cyan,
    blue = blue,
    indigo = indigo,
    purple = purple,
    pink = pink,
    brown = brown,

    gray = gray,
    gray2 = gray2,
    gray3 = gray3,
    gray4 = gray4,
    gray5 = gray5,
    gray6 = gray6,

    forceWhitePrimary = forceWhitePrimary,
    forceWhiteSecondary = forceWhitePrimary,
    forceWhiteTertiary = forceWhiteTertiary,

    forceDarkPrimary = forceDarkPrimary,
    forceDarkSecondary = forceDarkSecondary,
    forceDarkTertiary = forceDarkTertiary,
    forceDarkQuaternary = forceDarkQuaternary,

    backgroundPrimary = backgroundPrimary,
    backgroundSecondary = backgroundSecondary,
    backgroundTertiary =  backgroundTertiary,
    backgroundElevatedPrimary = backgroundElevatedPrimary,
    backgroundElevatedSecondary = backgroundElevatedSecondary,
    backgroundElevatedTertiary = backgroundElevatedTertiary,

    labelPrimary = forceWhitePrimary,
    labelSecondary = forceWhiteSecondary,
    labelTertiary = forceWhiteTertiary,
    labelQuaternary = forceWhiteQuaternary,

    transparent = Color(0x00000000),
    isDark = true
)

val LocalColors = staticCompositionLocalOf { darkColors() }