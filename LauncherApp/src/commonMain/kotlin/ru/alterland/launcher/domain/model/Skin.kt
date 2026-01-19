package ru.alterland.launcher.domain.model

data class Skin(
    val id: String,
    val name: String,
    val url: String,
    val modelType: ModelType,
    val isPublic: Boolean = true
) {
    enum class ModelType {
        WIDE, SLIM
    }
}
