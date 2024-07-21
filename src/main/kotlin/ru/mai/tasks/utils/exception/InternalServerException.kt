package ru.mai.tasks.utils.exception

import org.springframework.http.HttpStatus
import ru.mai.tasks.utils.exception.AbstractApiException

class InternalServerException : AbstractApiException(
    status = HttpStatus.INTERNAL_SERVER_ERROR,
    message = "Что-то пошло не так"
)
