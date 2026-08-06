package com.bytepowerlabs.safeops_api.modules.identity.security

import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm
import org.springframework.security.oauth2.jwt.JwsHeader
import org.springframework.security.oauth2.jwt.JwtClaimsSet
import org.springframework.security.oauth2.jwt.JwtEncoder
import org.springframework.security.oauth2.jwt.JwtEncoderParameters
import org.springframework.stereotype.Component
import java.time.Duration
import java.util.UUID
import java.time.Instant

data class GeneratedTokenResponse(
    val token: String,
    val expiresAt: Instant,
)

@Component
class JwtTokenGenerator(private val jwtEncoder: JwtEncoder) {
    fun generate(userAccountId: UUID, authSessionId: UUID): GeneratedTokenResponse {
        val issuedAt = Instant.now()
        val expiresAt = issuedAt.plus(ACCESS_TOKEN_DURATION)

        val header = JwsHeader.with(SignatureAlgorithm.RS256).keyId(KEY_ID).build()
        val claims = JwtClaimsSet.builder().issuer(ISSUER_ID).subject(userAccountId.toString()).issuedAt(issuedAt)
            .expiresAt(expiresAt)
            .id(UUID.randomUUID().toString()).claim("sid", authSessionId.toString()).build()

        val token = jwtEncoder.encode(JwtEncoderParameters.from(header, claims))

        return GeneratedTokenResponse(
            token = token.tokenValue,
            expiresAt = expiresAt
        )
    }

    private companion object {
        const val KEY_ID = "safeops-api-key"
        const val ISSUER_ID = "safeops-api"
        val ACCESS_TOKEN_DURATION: Duration = Duration.ofMinutes(15)
    }
}