package ru.mai.tasks.utils.exception

import org.springframework.http.HttpStatus
import ru.mai.tasks.utils.exception.AbstractApiException

class AccessDeniedException : AbstractApiException(
    status = HttpStatus.FORBIDDEN,
    message = "Доступ запрещен"
)