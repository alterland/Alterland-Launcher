plugins {
    alias(libs.plugins.multiplatform)
    alias(libs.plugins.buildConfig)
    alias(libs.plugins.kotlinx.serialization)
}

buildConfig {
    //buildConfigField("String", "CLIENT_API_BASE_URL", "\"client.alterland.ru\"")
    buildConfigField("String", "CLIENT_API_BASE_URL", "\"localhost:3200\"")
    buildConfigField("String", "MOJANG_ASSETS_HOST", "\"https://resources.download.minecraft.net\"")
    buildConfigField("String", "WORK_FOLDER", "\"alterland\"")
    buildConfigField("String", "SERVER_PROFILES_FOLDER", "\"server-profiles\"")
    buildConfigField("String", "CLIENT_PROFILES_FOLDER", "\"client-profiles\"")

    useKotlinOutput { internalVisibility = true }
}

@OptIn(org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi::class)
kotlin {
    applyDefaultHierarchyTemplate()

    jvm("desktop")

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.ktor.core)
                implementation(libs.ktor.negotiation)
                implementation(libs.ktor.json)
                implementation(libs.ktor.serialization)
                implementation(libs.ktor.kotlin.json)
                implementation(libs.ktor.logging)
                implementation(libs.ktor.client.okhttp)
                implementation(libs.koin.core)
                implementation(libs.dnsjava)
                implementation(libs.log4j)
            }
        }

        val desktopMain by getting {
            dependencies {
                implementation(libs.ktor.client.okhttp)
            }
        }

    }
}
