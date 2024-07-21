package ru.mai.tasks.utils.token

import ru.mai.tasks.models.entity.user.Role
import org.springframework.security.core.Authentication

class JwtAuthData(
    private var authenticated: Boolean,
    private val userId: Long,
    private val role: Role
) : Authentication {
    override fun getAuthorities() = setOf(role)
    override fun getName() = null
    override fun getCredentials() = null
    override fun getDetails() = null
    override fun getPrincipal() = userId
    override fun isAuthenticated() = authenticated
    override fun setAuthenticated(isAuthenticated: Boolean) {
        authenticated = isAuthenticated
    }
}