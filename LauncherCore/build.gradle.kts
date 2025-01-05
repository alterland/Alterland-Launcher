plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.kotlinxSerialization)
    alias(libs.plugins.buildConfig)
}

buildConfig {
    buildConfigField("boolean", "DEV_ENV", "${true}")
    buildConfigField("String", "DEV_API_BASE_URL", "\"localhost:3000\"")
    buildConfigField("String", "PROD_API_BASE_URL", "\"client.alterland.ru/api\"")
    buildConfigField("String", "MOJANG_ASSETS_HOST", "\"https://resources.download.minecraft.net\"")
    buildConfigField("String", "CLIENT_PROFILES_FOLDER", "\"client-profiles\"")
    buildConfigField("String", "WORK_FOLDER", "\"alterland\"")
    buildConfigField("boolean", "MATCH_LAUNCHER_FOLDER", "${false}")

    useKotlinOutput { internalVisibility = true }
}

kotlin {
    jvm("desktop")

    sourceSets {
        val desktopMain by getting

        commonMain.dependencies {
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.negotiation)
            implementation(libs.ktor.json)
            implementation(libs.ktor.serialization)
            implementation(libs.ktor.kotlin.json)
            implementation(libs.ktor.network)
            implementation(libs.ktor.logging)
            implementation(libs.koin.core)
            implementation(libs.dnsjava)
            implementation(libs.logback)
        }
        desktopMain.dependencies {
            implementation(libs.kotlinx.coroutines.swing)
            implementation(libs.ktor.client.okhttp)
        }
    }
}
