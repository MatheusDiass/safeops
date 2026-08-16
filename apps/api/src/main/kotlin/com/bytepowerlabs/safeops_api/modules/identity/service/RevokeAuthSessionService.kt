package com.bytepowerlabs.safeops_api.modules.identity.service

import com.bytepowerlabs.safeops_api.modules.identity.repository.AuthSessionRepository
import com.bytepowerlabs.safeops_api.modules.identity.repository.RefreshTokenRepository
import com.bytepowerlabs.safeops_api.modules.identity.security.RefreshTokenGenerator
import jakarta.transaction.Transactional
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class RevokeAuthSessionService(
    private val refreshTokenGenerator: RefreshTokenGenerator,
    private val refreshTokenRepository: RefreshTokenRepository,
) {
    @Transactional
    fun execute(refreshToken: String) {
        val refreshTokenHash = refreshTokenGenerator.hash(refreshToken)
        val refreshTokenEntity = refreshTokenRepository.findByTokenHash(refreshTokenHash) ?: return

        if(refreshTokenEntity.usedAt != null) {
            return
        }

        if (refreshTokenEntity.authSession.revokedAt != null) {
            return
        }

        refreshTokenEntity.authSession.revoke()
    }
}