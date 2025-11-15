package ru.alterland.launcher

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.dialogs.init
import org.koin.core.context.GlobalContext.startKoin
import ru.alterland.launcher.di.androidModule
import ru.alterland.launcher.di.commonModule

class AndroidApp : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            modules(commonModule)
            modules(androidModule())
        }
    }
}

class AppActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FileKit.init(this)
        setContent {
            App()
        }
    }
}
