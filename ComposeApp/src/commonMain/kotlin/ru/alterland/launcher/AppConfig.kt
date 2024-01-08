package ru.alterland.launcher

import AlterlandLauncher.ComposeApp.BuildConfig

object AppConfig {
    val apiBaseUrl: String
        get() = if (BuildConfig.DEV_ENV) BuildConfig.DEV_API_BASE_URL else BuildConfig.PROD_API_BASE_URL
}
