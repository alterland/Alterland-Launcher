import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlinxSerialization)
    alias(libs.plugins.buildConfig)
}

buildConfig {
    buildConfigField("boolean", "DEV_ENV", "${false}") //used to store cookies
    buildConfigField("String", "DEV_API_BASE_URL", "\"localhost:3000\"")
    buildConfigField("String", "PROD_API_BASE_URL", "\"alterland.ru/api\"")
    buildConfigField("String", "WORK_FOLDER", "\"alterland\"")

    useKotlinOutput { internalVisibility = true }
}

kotlin {
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }

    jvm("desktop")

    sourceSets {
        val desktopMain by getting

        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.appcompat)
            implementation(libs.androidx.core.ktx)
            implementation(libs.androidx.activity.compose)
            implementation(libs.kotlinx.coroutines.android)
            implementation(libs.ktor.client.okhttp)
        }
        commonMain.dependencies {
            implementation(project(":LauncherCore"))

            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.composeImageLoader)

            implementation(libs.voyager.navigator)
            implementation(libs.voyager.transitions)
            implementation(libs.voyager.koin)

            implementation(libs.ktor.client.core)
            implementation(libs.ktor.negotiation)
            implementation(libs.ktor.json)
            implementation(libs.ktor.kotlin.json)
            implementation(libs.ktor.serialization)
            implementation(libs.ktor.logging)

            implementation(libs.logback)

            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.kotlinx.datetime)

            implementation(libs.koin.core)

            implementation(libs.kstore)
            implementation(libs.kstore.file)
        }
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutines.swing)
            implementation(libs.ktor.client.okhttp)
        }
    }
}

android {
    namespace = "ru.alterland.launcher"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "ru.alterland.launcher.androidApp"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
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

dependencies {
    debugImplementation(compose.uiTooling)
}

compose.desktop {
    application {
        mainClass = "ru.alterland.launcher.MainKt"

        buildTypes.release {
            javaHome = "C:\\Users\\Roman\\Documents\\OpenJDK17U-jdk_x64_windows_hotspot_17.0.13_11\\jdk-17.0.13+11"
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
            packageVersion = "1.1.3"
            description = "Access to the Alterland world"
            vendor = "Alterland"

            includeAllModules = true

            //Use Other installer for Windows. E.g.: Inno Setup. IMPORTANT! - copy upgradeUuid value to the custom installer.
            windows {
                upgradeUuid = "375BA0BB-0A64-41A3-8D75-37315D837DE1"
                shortcut = true
            }
            macOS {
                bundleID = "ru.alterland.launcher"
            }
        }
    }
}
