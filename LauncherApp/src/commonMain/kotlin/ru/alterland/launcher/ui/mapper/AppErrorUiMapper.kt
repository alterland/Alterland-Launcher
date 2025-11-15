package ru.alterland.launcher.ui.mapper

import ru.alterland.launcher.domain.model.AppError
import ru.alterland.launcher.ui.model.AppErrorUi

fun AppError.toUi() = AppErrorUi(
    id = id,
    message = throwable.message.orEmpty()
)
