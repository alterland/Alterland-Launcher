package ru.alterland.launcher.ui.screen.main.clientsettings

sealed class RamSettings {
    data class FixedRamSettings(val value: String): RamSettings()
    data class CustomRamSettings(
        val value: Float,
        val steps: Int,
        val min: Float,
        val max: Float
    ): RamSettings()
}
