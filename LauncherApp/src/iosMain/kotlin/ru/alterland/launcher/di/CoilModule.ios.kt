package ru.alterland.launcher.di

import coil3.PlatformContext
import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.cacheDir
import io.github.vinceglb.filekit.path
import okio.Path.Companion.toPath
import org.koin.dsl.module

internal val iosCoilModule = module {
    single {
        createImageLoader(
            context = PlatformContext.INSTANCE,
            cachePath = FileKit.cacheDir.path.toPath()
        )
    }
}
