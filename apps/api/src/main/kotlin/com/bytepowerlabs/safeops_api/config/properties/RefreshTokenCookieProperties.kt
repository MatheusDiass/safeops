package com.bytepowerlabs.safeops_api.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "security.refresh-token.cookie")
data class RefreshTokenCookieProperties(
    val secure: Boolean,
    val sameSite: String,
)
