package ru.mai.tasks.models.dto.response.auth

import ru.mai.tasks.models.entity.token.Token

data class AuthTokenInfo(
    val refreshToken: String,
    val accessToken: String,
    val registration: Boolean
)

fun AuthTokenInfo.toEntity(userId: Long) = Token(
    userId = userId,
    refreshToken = refreshToken,
    accessToken = accessToken
)

fun Token.toDto() = AuthTokenInfo(
    refreshToken = refreshToken,
    accessToken = accessToken,
    registration = false
)