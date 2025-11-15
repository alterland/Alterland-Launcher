package ru.alterland.launcher.util.base

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class ErrorBody(
    @SerialName("message") val message: String?,
    @SerialName("reasons") val reasons: List<String>?
)

@Serializable
class FieldError(
    @SerialName("field") val field: String,
    @SerialName("messages") val error: List<String>
)
