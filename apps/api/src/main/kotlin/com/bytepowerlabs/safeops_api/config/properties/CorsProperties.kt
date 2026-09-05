package com.bytepowerlabs.safeops_api.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "security.cors")
data class CorsProperties(
    val allowedOrigins: List<String>,
)
