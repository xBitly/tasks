package ru.mai.tasks.utils.exception

import org.springframework.http.HttpStatus
import ru.mai.tasks.utils.exception.AbstractApiException

class NotFoundException(message: String) : AbstractApiException(
    status = HttpStatus.NOT_FOUND,
    message = message
)
