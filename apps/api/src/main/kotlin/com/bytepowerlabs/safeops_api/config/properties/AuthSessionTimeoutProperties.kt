package com.bytepowerlabs.safeops_api.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import java.time.Duration

@ConfigurationProperties(prefix = "security.auth-session")
data class AuthSessionTimeoutProperties(
    val absoluteTimeout: Duration,
    val inactivityTimeout: Duration,
)
