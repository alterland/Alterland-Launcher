plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.buildConfig)
    alias(libs.plugins.kotlinx.serialization)
}

buildConfig {
    buildConfigField("String", "CLIENT_API_BASE_URL", "\"client.alterland.ru/api\"")
    //buildConfigField("String", "CLIENT_API_BASE_URL", "\"localhost:3200\"")
    buildConfigField("String", "MOJANG_ASSETS_HOST", "\"https://resources.download.minecraft.net\"")
    buildConfigField("String", "WORK_FOLDER", "\"alterland\"")
    buildConfigField("String", "SERVER_PROFILES_FOLDER", "\"server-profiles\"")
    buildConfigField("String", "CLIENT_PROFILES_FOLDER", "\"client-profiles\"")

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
            implementation(libs.ktor.logging)
            implementation(libs.koin.core)
            implementation(libs.dnsjava)
            implementation(libs.log4j)
        }
        desktopMain.dependencies {
            implementation(libs.ktor.client.okhttp)
            implementation(libs.kotlinx.coroutines.swing)
        }
    }
}
