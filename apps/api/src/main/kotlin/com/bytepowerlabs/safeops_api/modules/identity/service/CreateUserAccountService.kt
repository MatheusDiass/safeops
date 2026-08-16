package com.bytepowerlabs.safeops_api.modules.identity.service

import com.bytepowerlabs.safeops_api.modules.identity.security.PasswordPolicy
import com.bytepowerlabs.safeops_api.modules.identity.dto.CreateUserAccountRequest
import com.bytepowerlabs.safeops_api.modules.identity.entity.UserAccountEntity
import com.bytepowerlabs.safeops_api.modules.identity.exception.EmailAlreadyExistsException
import com.bytepowerlabs.safeops_api.modules.identity.repository.UserRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CreateUserAccountService(
    private val repository: UserRepository,
    private val passwordPolicy: PasswordPolicy,
    private val passwordEncoder: PasswordEncoder
) {
    @Transactional
    fun execute(request: CreateUserAccountRequest) {
        val name = request.name.trim()
        val email = request.email.trim().lowercase()

        val userAccount = repository.existsByEmail(request.email)

        if (userAccount) {
            throw EmailAlreadyExistsException()
        }

        passwordPolicy.validate(request.password)

        val passwordHash = passwordEncoder.encode(request.password)

        val userAccountEntity = UserAccountEntity(
            name = name,
            email = email,
            passwordHash = passwordHash
        )

        repository.save(userAccountEntity)
    }
}