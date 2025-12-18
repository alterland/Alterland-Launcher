package ru.alterland.launcher.di

import android.content.Context

internal fun Context.androidModule() = listOf(
    platformModule()
)
