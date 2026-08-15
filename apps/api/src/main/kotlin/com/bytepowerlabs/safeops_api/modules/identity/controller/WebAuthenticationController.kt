package com.bytepowerlabs.safeops_api.modules.identity.controller

import com.bytepowerlabs.safeops_api.config.properties.RefreshTokenCookieProperties
import com.bytepowerlabs.safeops_api.modules.identity.dto.AuthenticateUserAccountRequest
import com.bytepowerlabs.safeops_api.modules.identity.dto.AuthenticateUserAccountResponse
import com.bytepowerlabs.safeops_api.modules.identity.service.AuthenticateUserAccountService
import com.bytepowerlabs.safeops_api.modules.identity.service.RevokeAuthSessionService
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseCookie
import org.springframework.web.bind.annotation.CookieValue
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.time.Duration
import java.time.Instant

@RestController
@RequestMapping("/identity/auth/web")
class WebAuthenticationController(
    private val revokeAuthSessionService: RevokeAuthSessionService,
    private val authenticateUserAccountService: AuthenticateUserAccountService,
    private val refreshTokenCookieProperties: RefreshTokenCookieProperties
) {
    companion object {
        private const val COOKIE_NAME = "refresh_token"
        private const val COOKIE_PATH = "/identity/auth/web"
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.ACCEPTED)
    fun login(
        @Valid @RequestBody request: AuthenticateUserAccountRequest,
        response: HttpServletResponse
    ): AuthenticateUserAccountResponse {
        val result = authenticateUserAccountService.execute(request)

        response.addHeader(
            HttpHeaders.SET_COOKIE,
            createRefreshTokenCookie(result.refreshToken, result.refreshTokenExpiresAt).toString()
        )

        return AuthenticateUserAccountResponse(
            accessToken = result.accessToken,
            accessTokenExpiresAt = result.accessTokenExpiresAt,
        )
    }

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun logout(@CookieValue(COOKIE_NAME, required = false) refreshToken: String?, response: HttpServletResponse) {
        if (refreshToken != null) {
            revokeAuthSessionService.execute(refreshToken)
        }

        response.addHeader(
            HttpHeaders.SET_COOKIE,
            createExpiredRefreshTokenCookie().toString()
        )
    }

    private fun createRefreshTokenCookie(refreshToken: String, expiresAt: Instant): ResponseCookie {
        return ResponseCookie.from(COOKIE_NAME, refreshToken)
            .httpOnly(true)
            .secure(refreshTokenCookieProperties.secure)
            .sameSite(refreshTokenCookieProperties.sameSite)
            .path(COOKIE_PATH)
            .maxAge(Duration.between(Instant.now(), expiresAt))
            .build()
    }

    private fun createExpiredRefreshTokenCookie(): ResponseCookie {
        return ResponseCookie.from(COOKIE_NAME, "")
            .httpOnly(true)
            .secure(refreshTokenCookieProperties.secure)
            .sameSite(refreshTokenCookieProperties.sameSite)
            .path(COOKIE_PATH)
            .maxAge(Duration.ZERO)
            .build()
    }
}