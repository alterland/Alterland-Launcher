package ru.alterland.launcher.di

import android.content.Context

fun Context.androidModule() = listOf(
    platformModule()
)
