package ru.mai.tasks.utils.exception

import org.springframework.http.HttpStatus
import ru.mai.tasks.utils.exception.AbstractApiException

class InvalidPasswordException : AbstractApiException(
    status = HttpStatus.CONFLICT,
    message = "Неверный пароль"
)