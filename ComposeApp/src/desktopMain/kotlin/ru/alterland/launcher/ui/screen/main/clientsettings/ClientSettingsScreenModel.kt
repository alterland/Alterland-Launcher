package ru.alterland.launcher.ui.screen.main.clientsettings

import ru.alterland.launcher.domain.repository.LocalStorage
import ru.alterland.launcher.ui.base.BaseScreenModel
import ru.alterland.launchercore.util.USER_HOME
import java.awt.Toolkit
import kotlin.io.path.Path

class ClientSettingsScreenModel(
    private val localStorage: LocalStorage
) : BaseScreenModel<ClientSettingsContract.Event, ClientSettingsContract.State, ClientSettingsContract.Effect>(
    initialState = ClientSettingsContract.State()
) {
    private val workFolder = Path("$USER_HOME/${ru.alterland.launcher.BuildConfig.WORK_FOLDER}").toString()

    private val recommendedCustomRam = RamSettings.CustomRamSettings(TEMP_RECOMMENDED_MEM, 16, 1024f, 16384f)

    init {
        initRamSettings()
        setState { copy(directoryPath = workFolder)}
        setState { copy(width = Toolkit.getDefaultToolkit().screenSize.width.toString(), height = Toolkit.getDefaultToolkit().screenSize.height.toString()) }
    }



    override fun onEvent(event: ClientSettingsContract.Event) {
        when(event) {
            is ClientSettingsContract.Event.OnRamSliderValueSelected -> handleRamSliderValueSelected(event.value)
            ClientSettingsContract.Event.OnUseRecommendedRamValueClick -> handleOnUseRecommendedRamValueClick()
            is ClientSettingsContract.Event.OnLaunchAfterUpdate -> handleOnLaunchAfterUpdate()
            is ClientSettingsContract.Event.OnLaunchFullScreen -> handleOnLaunchFullScreen()
            is ClientSettingsContract.Event.OnAutoConnect -> handleOnAutoConnect()
            is ClientSettingsContract.Event.OnDefaultDirectory -> handleDefaultDirectory()

            is ClientSettingsContract.Event.OnWidthInput -> setState { copy(width = event.data) }
            is ClientSettingsContract.Event.OnHeightInput -> setState { copy(height = event.data) }

            is ClientSettingsContract.Event.OnPathChange -> setState { copy(directoryPath = event.path)}
            is ClientSettingsContract.Event.OnInputRamSettings -> setState { copy(inputRamSettings = event.data)}
        }
    }

    private fun handleRamSliderValueSelected(value: Float) {
        setState { copy(ramSettings = recommendedCustomRam.copy(value = value)) }
    }

    private fun handleOnUseRecommendedRamValueClick() {
        setState { copy(ramSettings = recommendedCustomRam, useRecommendedRamValue = !useRecommendedRamValue) }
    }

    private fun handleOnLaunchAfterUpdate() {
        setState { copy(launchAfterUpdate = !launchAfterUpdate) }
    }

    private fun handleOnAutoConnect() {
        setState { copy(autoConnect = !autoConnect) }
    }

    private fun handleOnLaunchFullScreen() {
        setState {
            copy(
                launchFullScreen = !launchFullScreen,
                width = if (!launchFullScreen) {
                    Toolkit.getDefaultToolkit().screenSize.width.toString()
                } else {
                    "1920"
                },
                height = if (!launchFullScreen) {
                    Toolkit.getDefaultToolkit().screenSize.height.toString()
                } else {
                    "1080"
                }
            )
        }
    }

    private fun handleDefaultDirectory() {
        setState {
            copy(
                defaultDirectory = !defaultDirectory,
                directoryPath = if (!defaultDirectory) {
                    workFolder
                } else {
                    ""
                }
            )
        }
    }

    private fun initRamSettings() {
        setState { copy(ramSettings = recommendedCustomRam) }
    }

    companion object {
        private const val TEMP_RECOMMENDED_MEM = 6445.0f
    }
}
