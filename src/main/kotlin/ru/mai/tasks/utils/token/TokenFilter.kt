package ru.mai.tasks.utils.token

import ru.mai.tasks.models.entity.user.Role
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils
import org.springframework.web.filter.GenericFilterBean
import ru.mai.tasks.repository.TokenRepository

@Component
class TokenFilter(
    @Autowired private val tokenManager: TokenManager,
    @Autowired private val tokenRepo: TokenRepository
) : GenericFilterBean() {
    private val authHeaderName = "Authorization"
    private val authHeaderStart = "Bearer "

    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        (request as HttpServletRequest).getHeader(authHeaderName)?.let { header ->
            if (StringUtils.hasText(header) && header.startsWith(authHeaderStart)) {
                val token = header.substring(authHeaderStart.length)
                if (tokenManager.validateAccessToken(token)) {
                    tokenManager.getAccessClaims(token).let { claims ->
                        if (tokenRepo.getTokenByUserId(claims.subject.toLong())?.accessToken == token) {
                            val authData = JwtAuthData(
                                userId = claims.subject.toLong(),
                                role = claims.get("roles", String::class.java).let { Role.valueOf(it) },
                                authenticated = true
                            )
                            SecurityContextHolder.getContext().authentication = authData
                        }
                    }
                }
            }
        }
        chain.doFilter(request, response)
    }
}