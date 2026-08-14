package com.bytepowerlabs.safeops_api.modules.identity.service

import com.bytepowerlabs.safeops_api.modules.identity.repository.AuthSessionRepository
import com.bytepowerlabs.safeops_api.modules.identity.security.RefreshTokenGenerator
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.time.Instant

@Service
class RevokeAuthSessionService(
    private val refreshTokenGenerator: RefreshTokenGenerator,
    private val authSessionRepository: AuthSessionRepository
) {
    @Transactional
    fun execute(refreshToken: String) {
        val refreshTokenHash = refreshTokenGenerator.hash(refreshToken)
        val authSession = authSessionRepository.findByRefreshTokenHash(refreshTokenHash) ?: return

        if (authSession.revokeAt != null) {
            return
        }

        authSession.revoke()
    }
}