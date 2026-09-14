package com.bytepowerlabs.safeops_api.modules.organization.service

import com.bytepowerlabs.safeops_api.modules.identity.exception.UserAccountNotFoundException
import com.bytepowerlabs.safeops_api.modules.identity.repository.UserRepository
import com.bytepowerlabs.safeops_api.modules.organization.dto.CreateOrganizationRequest
import com.bytepowerlabs.safeops_api.modules.organization.dto.OrganizationResponse
import com.bytepowerlabs.safeops_api.modules.organization.entity.OrganizationEntity
import com.bytepowerlabs.safeops_api.modules.organization.entity.OrganizationMembershipEntity
import com.bytepowerlabs.safeops_api.modules.organization.entity.OrganizationRole
import com.bytepowerlabs.safeops_api.modules.organization.repository.OrganizationMembershipRepository
import com.bytepowerlabs.safeops_api.modules.organization.repository.OrganizationRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class CreateOrganizationService(
    private val organizationRepository: OrganizationRepository,
    private val organizationMembershipRepository: OrganizationMembershipRepository,
    private val userAccountRepository: UserRepository
) {
    @Transactional
    fun execute(request: CreateOrganizationRequest, userAccountId: UUID): OrganizationResponse {
        val userAccount = userAccountRepository.findByIdOrNull(userAccountId) ?: throw UserAccountNotFoundException()

        val organization = OrganizationEntity(name = request.name)

        val savedOrganization = organizationRepository.save(organization)

        val organizationMembershipEntity =
            OrganizationMembershipEntity(
                organization = organization,
                userAccount = userAccount,
                role = OrganizationRole.OWNER
            )

        organizationMembershipRepository.save(organizationMembershipEntity)

        return OrganizationResponse(
            id = savedOrganization.id,
            name = savedOrganization.name,
            status = savedOrganization.status,
            siteCount = 0,
            createdAt = savedOrganization.createdAt,
            updatedAt = savedOrganization.updatedAt,
        )
    }
}