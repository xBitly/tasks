package ru.mai.tasks.utils.exception

import org.springframework.http.HttpStatus
import ru.mai.tasks.utils.exception.AbstractApiException

class NotValidDataException : AbstractApiException(
    status = HttpStatus.CONFLICT,
    message = "Неккоректные данные"
)