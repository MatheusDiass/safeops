package com.bytepowerlabs.safeops_api.modules.organization.service

import com.bytepowerlabs.safeops_api.modules.organization.dto.GetOrganizationResponse
import com.bytepowerlabs.safeops_api.modules.organization.exception.OrganizationNotFoundException
import com.bytepowerlabs.safeops_api.modules.organization.repository.OrganizationMembershipRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class GetOrganizationService(private val membershipRepository: OrganizationMembershipRepository) {
    @Transactional(readOnly = true)
    fun execute(organizationId: UUID, userAccountId: UUID): GetOrganizationResponse {
        val membership =
            membershipRepository.findByOrganizationIdAndUserAccountId(organizationId, userAccountId)
                ?: throw OrganizationNotFoundException()

        val organization = membership.organization

        return GetOrganizationResponse(
            id = organization.id,
            name = organization.name,
            status = organization.status,
            createdAt = organization.createdAt,
            updatedAt = organization.updatedAt,
        )
    }
}