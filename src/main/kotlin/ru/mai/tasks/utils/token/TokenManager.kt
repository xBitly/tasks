package ru.mai.tasks.utils.token

import ru.mai.tasks.models.entity.user.User
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.security.Key
import java.util.*

@Component
class TokenManager(
    @Value("\${jwt.secret.access}") jwtSecretAccess: String,
    @Value("\${jwt.secret.refresh}") jwtSecretRefresh: String
) {
    private val jwtSecretAccess = Keys.hmacShaKeyFor(jwtSecretAccess.toByteArray())
    private val jwtSecretRefresh = Keys.hmacShaKeyFor(jwtSecretRefresh.toByteArray())

    private val accessTokenLimit = 60 * 60 * 24
    private val refreshTokenLimit = 60 * 60 * 24 * 30

    fun generateAccessToken(user: User): String {
        return Date().let { date ->
            Jwts.builder()
                .setSubject(user.id.toString())
                .setIssuedAt(date)
                .setExpiration(Date(date.time + accessTokenLimit * 1000L))
                .claim("roles", user.role)
                .signWith(jwtSecretAccess)
                .compact()
        }
    }

    fun generateRefreshToken(user: User): String {
        return Date().let { date ->
            Jwts.builder()
                .setSubject(user.id?.toString())
                .setIssuedAt(date)
                .setExpiration(Date(date.time + refreshTokenLimit * 1000L))
                .signWith(jwtSecretRefresh)
                .compact()
        }
    }

    fun validateAccessToken(token: String) = validateToken(token, jwtSecretAccess)

    fun validateRefreshToken(token: String) = validateToken(token, jwtSecretRefresh)

    private fun validateToken(token: String, key: Key): Boolean {
        return try {
            Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
            true
        } catch (e: Exception) {
            println(e.message)
            false
        }
    }

    fun getAccessClaims(token: String) = getClaims(token, jwtSecretAccess)

    fun getRefreshClaims(token: String) = getClaims(token, jwtSecretRefresh)

    private fun getClaims(token: String, secret: Key): Claims {
        return Jwts.parserBuilder()
            .setSigningKey(secret)
            .build()
            .parseClaimsJws(token)
            .body
    }
}