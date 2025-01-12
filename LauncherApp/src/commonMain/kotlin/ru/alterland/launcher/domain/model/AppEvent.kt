package ru.alterland.launcher.domain.model

sealed class AppEvent {
    data object UpdateServerProfiles: AppEvent()
}
