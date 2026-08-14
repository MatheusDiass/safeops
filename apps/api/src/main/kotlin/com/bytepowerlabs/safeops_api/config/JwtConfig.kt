package com.bytepowerlabs.safeops_api.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.jwt.JwtEncoder
import org.springframework.security.oauth2.jwt.JwtValidators
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey

@ConfigurationProperties(prefix = "security.jwt")
data class JwtKeyProperties(
    val publicKey: RSAPublicKey,
    val privateKey: RSAPrivateKey
)

@Configuration
class JwtConfig(private val jwtProperties: JwtKeyProperties) {
    @Bean
    fun jwtEncoder(): JwtEncoder {
        return NimbusJwtEncoder.withKeyPair(jwtProperties.publicKey, jwtProperties.privateKey).jwkPostProcessor { key ->
            key.keyID("safeops-api-key")
        }.build()
    }

    @Bean
    fun jwtDecoder(): JwtDecoder {
        val decoder = NimbusJwtDecoder.withPublicKey(jwtProperties.publicKey).build()
        decoder.setJwtValidator(JwtValidators.createDefaultWithIssuer("safeops-api-key"))
        return decoder
    }
}