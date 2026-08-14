package com.bytepowerlabs.safeops_api.modules.identity.controller

import com.bytepowerlabs.safeops_api.config.properties.RefreshTokenCookieProperties
import com.bytepowerlabs.safeops_api.modules.identity.service.RevokeAuthSessionService
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseCookie
import org.springframework.web.bind.annotation.CookieValue
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.time.Duration

@RestController
@RequestMapping("/identity/auth/web")
class WebAuthenticationController(
    private val revokeAuthSessionService: RevokeAuthSessionService,
    private val refreshTokenCookieProperties: RefreshTokenCookieProperties
) {
    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun logout(@CookieValue("refresh_token", required = false) refreshToken: String?, response: HttpServletResponse) {
        if (refreshToken != null) {
            revokeAuthSessionService.execute(refreshToken)
        }

        response.addHeader(
            HttpHeaders.SET_COOKIE,
            createExpiredRefreshToken().toString()
        )
    }

    private fun createExpiredRefreshToken(): ResponseCookie {
        return ResponseCookie.from("refresh_token", "")
            .httpOnly(true)
            .secure(refreshTokenCookieProperties.secure)
            .sameSite(refreshTokenCookieProperties.sameSite)
            .path("/identity/auth/web")
            .maxAge(Duration.ZERO)
            .build()
    }
}