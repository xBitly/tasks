package ru.mai.tasks.models.dto.request.auth

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern

data class CredentialsRequest(
    @field:NotBlank
    @field:Pattern(regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")
    val email: String,

    @field:NotBlank
    val password: String
)