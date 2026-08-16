package com.bytepowerlabs.safeops_api.modules.identity.service

import com.bytepowerlabs.safeops_api.config.properties.AuthSessionTimeoutProperties
import com.bytepowerlabs.safeops_api.modules.identity.dto.AuthenticateUserAccountResult
import com.bytepowerlabs.safeops_api.modules.identity.entity.RefreshTokenEntity
import com.bytepowerlabs.safeops_api.modules.identity.exception.InvalidRefreshTokenException
import com.bytepowerlabs.safeops_api.modules.identity.exception.RefreshTokenReplayException
import com.bytepowerlabs.safeops_api.modules.identity.repository.RefreshTokenRepository
import com.bytepowerlabs.safeops_api.modules.identity.security.JwtTokenGenerator
import com.bytepowerlabs.safeops_api.modules.identity.security.RefreshTokenGenerator
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.time.Instant

@Service
class RefreshAuthSessionService(
    private val refreshTokenRepository: RefreshTokenRepository,
    private val refreshTokenGenerator: RefreshTokenGenerator,
    private val jwtTokenGenerator: JwtTokenGenerator,
    private val authSessionTimeoutProperties: AuthSessionTimeoutProperties
) {
    @Transactional(dontRollbackOn = [RefreshTokenReplayException::class])
    fun execute(refreshToken: String): AuthenticateUserAccountResult {
        val now = Instant.now()
        val refreshTokenHash = refreshTokenGenerator.hash(refreshToken)
        val refreshTokenEntity =
            refreshTokenRepository.findByTokenHash(refreshTokenHash) ?: throw InvalidRefreshTokenException()
        val authSession = refreshTokenEntity.authSession
        val userAccount = authSession.userAccount

        if (authSession.revokedAt != null) {
            throw InvalidRefreshTokenException()
        }

        if (!authSession.expiresAt.isAfter(now)) {
            throw InvalidRefreshTokenException()
        }

        if (refreshTokenEntity.usedAt != null) {
            authSession.revoke()
            throw RefreshTokenReplayException()
        }

        if (!refreshTokenEntity.expiresAt.isAfter(now)) {
            throw InvalidRefreshTokenException()
        }

        val refreshTokenExpiresAt =
            minOf(now.plus(authSessionTimeoutProperties.inactivityTimeout), authSession.expiresAt)

        val newAccessToken =
            jwtTokenGenerator.generate(userAccount.id, authSession.id)
        val newRefreshToken = refreshTokenGenerator.generate()

        val newRefreshTokenEntity = RefreshTokenEntity(
            authSession = authSession,
            tokenHash = newRefreshToken.hash,
            expiresAt = refreshTokenExpiresAt,
        )

        refreshTokenEntity.markAsUsed()
        refreshTokenRepository.flush()
        refreshTokenRepository.save(newRefreshTokenEntity)

        return AuthenticateUserAccountResult(
            accessToken = newAccessToken.token,
            accessTokenExpiresAt = newAccessToken.expiresAt,
            refreshToken = newRefreshToken.value,
            refreshTokenExpiresAt = refreshTokenExpiresAt,
        )
    }
}