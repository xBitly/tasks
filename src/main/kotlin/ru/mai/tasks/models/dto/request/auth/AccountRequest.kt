package ru.mai.tasks.models.dto.request.auth

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size
import ru.mai.tasks.models.entity.user.User

data class AccountRequest(
    @field:NotBlank
    @field:Pattern(regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")
    val email: String,

    @field:NotBlank
    @field:Size(min = 8)
    val password: String,

    @field:NotBlank
    val phone: String,

    @field:NotBlank
    val fullName: String
)

fun AccountRequest.toEntity() = User(
    email = email,
    password = password,
    phone = phone,
    fullName = fullName,
    tasks = mutableListOf()
)