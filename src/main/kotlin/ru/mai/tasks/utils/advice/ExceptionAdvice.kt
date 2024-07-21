package ru.mai.tasks.utils.advice

import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import ru.mai.tasks.models.dto.response.ApiResponse
import ru.mai.tasks.utils.exception.AbstractApiException
import ru.mai.tasks.utils.exception.AccessDeniedException
import ru.mai.tasks.utils.exception.InternalServerException
import ru.mai.tasks.utils.exception.NotValidDataException


@ControllerAdvice
class ExceptionAdvice {
    private val logger = LoggerFactory.getLogger(ExceptionAdvice::class.java)

    @ExceptionHandler(value = [AbstractApiException::class])
    protected fun handle(cause: AbstractApiException, request: WebRequest): ResponseEntity<ApiResponse> {
        logger.info(cause.stackTraceToString())
        return cause.asResponse()
    }

    @ExceptionHandler(value = [Throwable::class])
    protected fun handle(cause: Throwable, request: WebRequest): ResponseEntity<ApiResponse> {
        logger.error(cause.stackTraceToString())
        return InternalServerException().asResponse()
    }

    @ExceptionHandler(value = [org.springframework.security.access.AccessDeniedException::class])
    protected fun handle(cause: org.springframework.security.access.AccessDeniedException, request: WebRequest): ResponseEntity<ApiResponse> {
        logger.error(cause.stackTraceToString())
        return AccessDeniedException().asResponse()
    }

    @ExceptionHandler(value = [org.springframework.web.bind.MethodArgumentNotValidException::class])
    protected fun handle(cause: org.springframework.web.bind.MethodArgumentNotValidException, request: WebRequest): ResponseEntity<ApiResponse> {
        logger.error(cause.stackTraceToString())
        return NotValidDataException().asResponse()
    }
}