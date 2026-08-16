package com.bytepowerlabs.safeops_api.modules.identity.service

import com.bytepowerlabs.safeops_api.config.properties.AuthSessionTimeoutProperties
import com.bytepowerlabs.safeops_api.modules.identity.dto.AuthenticateUserAccountRequest
import com.bytepowerlabs.safeops_api.modules.identity.dto.AuthenticateUserAccountResult
import com.bytepowerlabs.safeops_api.modules.identity.entity.AccountStatus
import com.bytepowerlabs.safeops_api.modules.identity.entity.AuthSessionEntity
import com.bytepowerlabs.safeops_api.modules.identity.entity.RefreshTokenEntity
import com.bytepowerlabs.safeops_api.modules.identity.exception.InvalidCredentialsException
import com.bytepowerlabs.safeops_api.modules.identity.exception.UserAccountUnavailableException
import com.bytepowerlabs.safeops_api.modules.identity.repository.AuthSessionRepository
import com.bytepowerlabs.safeops_api.modules.identity.repository.RefreshTokenRepository
import com.bytepowerlabs.safeops_api.modules.identity.repository.UserRepository
import com.bytepowerlabs.safeops_api.modules.identity.security.JwtTokenGenerator
import com.bytepowerlabs.safeops_api.modules.identity.security.RefreshTokenGenerator
import jakarta.transaction.Transactional
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.time.Instant

@Service
class AuthenticateUserAccountService(
    private val userRepository: UserRepository,
    private val authSessionRepository: AuthSessionRepository,
    private val refreshTokenRepository: RefreshTokenRepository,
    private val passwordEncoder: PasswordEncoder,
    private val refreshTokenGenerator: RefreshTokenGenerator,
    private val jwtTokenGenerator: JwtTokenGenerator,
    private val authSessionTimeoutProperties: AuthSessionTimeoutProperties
) {
    @Transactional
    fun execute(request: AuthenticateUserAccountRequest): AuthenticateUserAccountResult {
        val userAccount = userRepository.findByEmail(request.email) ?: throw InvalidCredentialsException()

        if (!passwordEncoder.matches(request.password, userAccount.passwordHash)) {
            throw InvalidCredentialsException()
        }

        if (userAccount.status != AccountStatus.ACTIVE) {
            throw UserAccountUnavailableException()
        }

        val now = Instant.now()
        val authSessionExpiresAt = now.plus(authSessionTimeoutProperties.absoluteTimeout)
        val refreshTokenExpiresAt = now.plus(authSessionTimeoutProperties.inactivityTimeout)

        val refreshToken = refreshTokenGenerator.generate()

        val authSessionEntity = AuthSessionEntity(
            userAccount = userAccount,
            expiresAt = authSessionExpiresAt,
        )

        val savedAuthSession = authSessionRepository.save(authSessionEntity)

        val refreshTokenEntity = RefreshTokenEntity(
            authSession = savedAuthSession,
            tokenHash = refreshToken.hash,
            expiresAt = refreshTokenExpiresAt,
        )

        refreshTokenRepository.save(refreshTokenEntity)

        val accessToken = jwtTokenGenerator.generate(userAccount.id, savedAuthSession.id)

        return AuthenticateUserAccountResult(
            accessToken = accessToken.token,
            accessTokenExpiresAt = accessToken.expiresAt,
            refreshToken = refreshToken.value,
            refreshTokenExpiresAt = refreshTokenExpiresAt
        )
    }
}