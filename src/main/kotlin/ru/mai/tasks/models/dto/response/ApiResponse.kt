package ru.mai.tasks.models.dto.response

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import java.time.LocalDateTime

interface ApiResponse {
    val status: HttpStatus
    val message: String
    val timestamp: LocalDateTime

    fun asResponse() = ResponseEntity.status(status).body(this)
}
