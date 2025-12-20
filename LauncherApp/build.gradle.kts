import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeHotReload)
    alias(libs.plugins.kotlinxSerialization)
    alias(libs.plugins.buildConfig)
}

buildConfig {
    buildConfigField("boolean", "DEV_ENV", "${false}")
    buildConfigField("String", "DEV_API_BASE_URL", "\"localhost:3000\"")
    buildConfigField("String", "PROD_API_BASE_URL", "\"api.alterland.ru\"")
    buildConfigField("String", "MOJANG_ASSETS_HOST", "\"https://resources.download.minecraft.net\"")
    buildConfigField("String", "WORK_FOLDER", "\"alterland\"")
    buildConfigField("String", "CLIENT_PROFILES_FOLDER", "\"client-profiles\"")
    buildConfigField("String", "SERVER_PROFILES_FOLDER", "\"server-profiles\"")

    useKotlinOutput { internalVisibility = true }

    packageName = "ru.alterland.launcher"
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "LauncherApp"
            isStatic = true
        }
    }

    jvm()

    sourceSets {
        androidMain.dependencies {
            implementation(libs.androidx.appcompat)
            implementation(libs.androidx.core.ktx)
            implementation(libs.androidx.activity.compose)
            implementation(libs.kotlinx.coroutines.android)
            implementation(libs.ktor.client.cio)
        }
        appleMain.dependencies {
            implementation(libs.ktor.client.darwin)
        }
        commonMain.dependencies {
            implementation(libs.compose.runtime)
            implementation(libs.compose.ui)
            implementation(libs.compose.material)
            implementation(libs.compose.foundation)
            implementation(libs.compose.ui.preview)
            implementation(libs.compose.components)
            implementation(libs.androidx.lifecycle.runtime)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.viewmodel.nav3)
            implementation(libs.androidx.nav3.ui)

            implementation(libs.ktor.client.core)
            implementation(libs.ktor.auth)
            implementation(libs.ktor.negotiation)
            implementation(libs.ktor.json)
            implementation(libs.ktor.kotlin.json)
            implementation(libs.ktor.serialization)
            implementation(libs.ktor.network)
            implementation(libs.ktor.logging)

            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.kotlinx.datetime)

            implementation(libs.orbit.core)
            implementation(libs.orbit.viewmodel)
            implementation(libs.orbit.compose)

            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)

            implementation(libs.kstore)
            implementation(libs.kstore.file)

            implementation(libs.coil.compose)
            implementation(libs.coil.ktor)

            implementation(libs.filekit.core)
            implementation(libs.filekit.dialogs)
            implementation(libs.filekit.dialogs.compose)
            implementation(libs.filekit.coil)

            implementation(libs.mcutils.server.status)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
        jvmMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutines.swing)
            implementation(libs.logback)
            implementation(libs.ktor.client.cio)
        }
    }
}

android {
    namespace = "ru.alterland.launcher"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "ru.alterland.launcher"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            excludes += "/META-INF/INDEX.LIST"
            excludes += "DebugProbesKt.bin"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

compose.desktop {
    application {
        mainClass = "ru.alterland.launcher.MainKt"

        buildTypes.release {
            proguard {
                isEnabled = true
                configurationFiles.from(project.file("compose-desktop.pro"))
                obfuscate = true
                optimize = true
                joinOutputJars = true
            }
        }

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "AlterlandLauncher"
            packageVersion = "1.4.0"
            description = "Access to the Alterland world"
            vendor = "Alterland"
            modules("java.instrument", "jdk.management", "jdk.unsupported")
            linux {
                iconFile.set(project.file("icon.png"))
                modules("jdk.security.auth")
            }

            //Use Other installer for Windows. E.g.: Inno Setup. IMPORTANT! - copy upgradeUuid value to the custom installer.
            windows {
                iconFile.set(project.file("icon.ico"))
                upgradeUuid = "375BA0BB-0A64-41A3-8D75-37315D837DE1"
                shortcut = true
            }
            macOS {
                iconFile.set(project.file("icon.icns"))
                bundleID = "ru.alterland.launcher"
            }
        }
    }
}
