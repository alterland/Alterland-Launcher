package ru.alterland.launcher.di

import android.content.Context
import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.filesDir
import io.github.vinceglb.filekit.path
import okio.Path.Companion.toPath
import org.koin.dsl.module

internal fun Context.androidCoilModule() = module {
    single {
        createImageLoader(
            context = this@androidCoilModule,
            cachePath = FileKit.filesDir.path.toPath()
        )
    }
}
