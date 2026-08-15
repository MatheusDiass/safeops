package com.bytepowerlabs.safeops_api.modules.identity.dto

import java.time.Instant

data class AuthenticateUserAccountResult(
    val accessToken: String,
    val accessTokenExpiresAt: Instant,
    val refreshToken: String,
    val refreshTokenExpiresAt: Instant,
)
