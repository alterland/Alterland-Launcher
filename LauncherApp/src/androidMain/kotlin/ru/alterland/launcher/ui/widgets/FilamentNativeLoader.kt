package ru.alterland.launcher.ui.widgets

import java.util.concurrent.atomic.AtomicBoolean

/**
 * Loads Filament JNI natives on Android from the AAR packaged shared libraries.
 */
internal object FilamentNativeLoader {
    private val loaded = AtomicBoolean(false)

    fun load() {
        if (loaded.compareAndSet(false, true)) {
            // Libraries are packaged inside the filament AARs.
            System.loadLibrary("filament-jni")
        }
    }
}
