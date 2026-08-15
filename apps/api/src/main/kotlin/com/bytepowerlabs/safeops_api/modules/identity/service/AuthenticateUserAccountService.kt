package com.bytepowerlabs.safeops_api.modules.identity.service

import com.bytepowerlabs.safeops_api.modules.identity.dto.AuthenticateUserAccountRequest
import com.bytepowerlabs.safeops_api.modules.identity.dto.AuthenticateUserAccountResult
import com.bytepowerlabs.safeops_api.modules.identity.entity.AccountStatus
import com.bytepowerlabs.safeops_api.modules.identity.entity.AuthSessionEntity
import com.bytepowerlabs.safeops_api.modules.identity.exception.InvalidCredentialsException
import com.bytepowerlabs.safeops_api.modules.identity.exception.UserAccountUnavailableException
import com.bytepowerlabs.safeops_api.modules.identity.repository.AuthSessionRepository
import com.bytepowerlabs.safeops_api.modules.identity.repository.UserRepository
import com.bytepowerlabs.safeops_api.modules.identity.security.JwtTokenGenerator
import com.bytepowerlabs.safeops_api.modules.identity.security.RefreshTokenGenerator
import jakarta.transaction.Transactional
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.time.Duration
import java.time.Instant

@Service
class AuthenticateUserAccountService(
    private val userRepository: UserRepository,
    private val authSessionRepository: AuthSessionRepository,
    private val passwordEncoder: PasswordEncoder,
    private val refreshTokenGenerator: RefreshTokenGenerator,
    private val jwtTokenGenerator: JwtTokenGenerator
) {
    @Transactional
    fun execute(request: AuthenticateUserAccountRequest): AuthenticateUserAccountResult {
        val userAccount = userRepository.findByEmail(request.email) ?: throw InvalidCredentialsException()

        if (!passwordEncoder.matches(request.password, userAccount.passwordHash)) {
            throw InvalidCredentialsException()
        }

        if(userAccount.status != AccountStatus.ACTIVE) {
            throw UserAccountUnavailableException()
        }

        val now = Instant.now()
        val refreshTokenDuration = now.plus(Duration.ofDays(30))

        val refreshToken = refreshTokenGenerator.generate()

        val authSession = AuthSessionEntity(
            userAccountId = userAccount.id,
            refreshTokenHash = refreshToken.hash,
            expiresAt = refreshTokenDuration,
            createdAt = now,
        )

        val savedAuthSession = authSessionRepository.save(authSession)

        val accessToken = jwtTokenGenerator.generate(userAccount.id, savedAuthSession.id)

        return AuthenticateUserAccountResult(
            accessToken = accessToken.token,
            accessTokenExpiresAt = accessToken.expiresAt,
            refreshToken = refreshToken.value,
            refreshTokenExpiresAt = refreshTokenDuration
        )
    }
}