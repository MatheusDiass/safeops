package com.bytepowerlabs.safeops_api.modules.identity.service

import com.bytepowerlabs.safeops_api.modules.identity.dto.GetAuthenticatedUserAccountResponse
import com.bytepowerlabs.safeops_api.modules.identity.exception.UserAccountNotFoundException
import com.bytepowerlabs.safeops_api.modules.identity.repository.UserRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class GetAuthenticatedUserAccountService(private val userAccountRepository: UserRepository) {
    @Transactional(readOnly = true)
    fun execute(userAccountId: UUID): GetAuthenticatedUserAccountResponse {
        val user = userAccountRepository.findByIdOrNull(userAccountId) ?: throw UserAccountNotFoundException()

        return GetAuthenticatedUserAccountResponse(
            id = user.id,
            name = user.name,
            email = user.email
        )
    }
}