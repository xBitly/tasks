package ru.mai.tasks.models.dto.request.auth

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class PasswordRequest(
    @field:NotBlank
    @field:Size(min = 8)
    val oldPassword: String,

    @field:NotBlank
    @field:Size(min = 8)
    val newPassword: String
)