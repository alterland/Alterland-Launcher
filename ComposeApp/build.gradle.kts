import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.multiplatform)
    alias(libs.plugins.compose)
    alias(libs.plugins.android.application)
    alias(libs.plugins.libres)
    alias(libs.plugins.buildConfig)
    alias(libs.plugins.kotlinx.serialization)
}

buildConfig {
    buildConfigField("boolean", "DEV_ENV", "${false}") //used to store cookies
    buildConfigField("String", "DEV_API_BASE_URL", "\"localhost:3000\"")
    buildConfigField("String", "PROD_API_BASE_URL", "\"alterland.ru/api\"")
    buildConfigField("String", "WORK_FOLDER", "\"alterland\"")

    useKotlinOutput { internalVisibility = true }
}

@OptIn(org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi::class)
kotlin {
    applyDefaultHierarchyTemplate()

    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = JavaVersion.VERSION_17.toString()
            }
        }
    }

    jvm("desktop")

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":LauncherCore"))
                implementation(compose.runtime)
                implementation(compose.material3)
                implementation(libs.compose.uitooling)
                implementation(libs.libres)
                implementation(libs.voyager.navigator)
                implementation(libs.voyager.transitions)
                implementation(libs.voyager.koin)
                implementation(libs.composeImageLoader)
                implementation(libs.napier)
                implementation(libs.ktor.core)
                implementation(libs.ktor.negotiation)
                implementation(libs.ktor.json)
                implementation(libs.ktor.kotlin.json)
                implementation(libs.ktor.serialization)
                implementation(libs.ktor.logging)
                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.kotlinx.datetime)
                implementation(libs.koin.core)
                implementation(libs.kstore)
                implementation(libs.kstore.file)
            }
        }

        val androidMain by getting {
            dependencies {
                implementation(libs.androidx.appcompat)
                implementation(libs.androidx.activityCompose)
                implementation(libs.kotlinx.coroutines.android)
                implementation(libs.ktor.client.okhttp)
            }
        }

        val desktopMain by getting {
            dependencies {
                implementation(compose.desktop.common)
                implementation(compose.desktop.currentOs)
                implementation(libs.ktor.client.okhttp)
            }
        }

    }
}

android {
    namespace = "ru.alterland.launcher"
    compileSdk = 34

    defaultConfig {
        minSdk = 26
        targetSdk = 34

        applicationId = "ru.alterland.launcher.androidApp"
        versionCode = 1
        versionName = "1.0.0"
    }
    sourceSets["main"].apply {
        manifest.srcFile("src/androidMain/AndroidManifest.xml")
        res.srcDirs("src/androidMain/resources")
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

compose.desktop {
    application {
        mainClass = "MainKt"

        buildTypes.release.proguard {
            isEnabled = true
            configurationFiles.from(project.file("compose-desktop.pro"))
            obfuscate = true
            optimize = true
        }

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "AlterlandLauncher"
            packageVersion = "1.0.0"
            description = "Access to the Alterland world"
            vendor = "Alterland"

            windows {
                shortcut = true
            }
            macOS {
                bundleID = "ru.alterland.launcher"
            }
        }
    }
}

libres {
//    generatedClassName = "MainRes" // "Res" by default
//    generateNamedArguments = true // false by default
    baseLocaleLanguageCode = "ru" // "en" by default
//    camelCaseNamesForAppleFramework = false // false by default
}
tasks.getByPath("desktopProcessResources").dependsOn("libresGenerateResources")
tasks.getByPath("desktopSourcesJar").dependsOn("libresGenerateResources")
