package ru.alterland.launcher.domain.model

data class Skin(
    val name: String,
    val url: String,
    val modelType: ModelType
) {
    enum class ModelType {
        WIDE, SLIM
    }
}
