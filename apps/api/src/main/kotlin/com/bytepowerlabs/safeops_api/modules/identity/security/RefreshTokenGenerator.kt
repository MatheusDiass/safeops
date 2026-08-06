package com.bytepowerlabs.safeops_api.modules.identity.security

import com.nimbusds.jose.util.StandardCharset
import org.springframework.stereotype.Component
import java.security.MessageDigest
import java.security.SecureRandom
import java.util.Base64
import java.util.HexFormat

data class GeneratedRefreshTokenResponse(
    val value: String,
    val hash: String,
)

@Component
class RefreshTokenGenerator {
    private val secureRandom = SecureRandom()

    fun generate(): GeneratedRefreshTokenResponse {
        val randomBytes = ByteArray(32)
        secureRandom.nextBytes(randomBytes)

        val value = Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes)

        return GeneratedRefreshTokenResponse(
            value = value,
            hash = hash(value)
        )
    }

    private fun hash(value: String): String {
        val digest = MessageDigest.getInstance("SHA-256").digest(value.toByteArray(StandardCharset.UTF_8))
        return HexFormat.of().formatHex(digest)
    }
}