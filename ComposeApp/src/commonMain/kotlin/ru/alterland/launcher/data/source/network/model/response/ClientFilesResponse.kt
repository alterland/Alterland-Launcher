package ru.alterland.launcher.data.source.network.model.response

import kotlinx.serialization.Serializable

@Serializable
data class ClientFilesResponse(
    val old: List<String>,
    val new: List<String>,
    val totalNewBytes: Long
)
