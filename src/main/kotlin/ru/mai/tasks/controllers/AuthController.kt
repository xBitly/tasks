package ru.mai.tasks.controllers

import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import ru.mai.tasks.models.dto.request.auth.*
import ru.mai.tasks.models.dto.response.auth.AuthTokenInfo
import ru.mai.tasks.services.AuthService

@Controller
@RequestMapping("api/v1/auth")
class AuthController(
    @Autowired private val authService: AuthService
) {
    @PostMapping("/signup")
    @ResponseBody
    @PreAuthorize("hasAuthority('ADMIN')")
    fun signup(@Valid @RequestBody request: AccountRequest, auth: Authentication): AuthTokenInfo {
        return authService.signup(request)
    }

    @PostMapping("/signin")
    @ResponseBody
    fun signin(@Valid @RequestBody request: CredentialsRequest): AuthTokenInfo {
        return authService.signin(request)
    }

    @PostMapping("/refresh")
    @ResponseBody
    fun refresh(@Valid @RequestBody request: RefreshTokenRequest): AuthTokenInfo {
        return authService.refresh(request.refreshToken)
    }

    @PostMapping("/signout")
    @ResponseBody
    fun signout(auth: Authentication) {
        authService.signout(auth.principal as Long)
    }

    @PostMapping("/password")
    @ResponseBody
    fun setPassword(@Valid @RequestBody request: PasswordRequest, auth: Authentication) {
        authService.setPassword(auth.principal as Long, request)
    }
}