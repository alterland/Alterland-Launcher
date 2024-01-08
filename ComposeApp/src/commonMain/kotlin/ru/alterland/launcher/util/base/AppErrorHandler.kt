package ru.alterland.launcher.util.base

import kotlinx.serialization.json.Json

fun throwAppError(jsonInstance: Json, json: String, statusCode: Int) {
    if (json.contains("[")) {
        val body = jsonInstance.decodeFromString<ValidationErrorBody>(json)
        throw AppException.createAppException(body.reasons)
    } else {
        val body = jsonInstance.decodeFromString<ErrorBody>(json)
        throw if (body.message.isEmpty()) {
            throw when(statusCode) {
                in 400..499 -> AppException.ClientException
                else -> AppException.ServerException
            }
        } else {
            AppException.createAppException(body.message)
        }
    }
}
