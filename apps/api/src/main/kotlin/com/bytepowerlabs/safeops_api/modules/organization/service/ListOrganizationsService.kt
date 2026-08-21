package com.bytepowerlabs.safeops_api.modules.organization.service

import com.bytepowerlabs.safeops_api.modules.organization.dto.OrganizationResponse
import com.bytepowerlabs.safeops_api.modules.organization.entity.OrganizationMembershipStatus
import com.bytepowerlabs.safeops_api.modules.organization.repository.OrganizationMembershipRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class ListOrganizationsService(private val membershipRepository: OrganizationMembershipRepository) {
    @Transactional(readOnly = true)
    fun execute(userAccountId: UUID): List<OrganizationResponse> {
        val memberships = membershipRepository.findAllByUserAccountIdAndStatus(
            userAccountId = userAccountId,
            status = OrganizationMembershipStatus.ACTIVE
        )

        return memberships.map { membership ->
            val organization = membership.organization
            OrganizationResponse(
                id = organization.id,
                name = organization.name,
                status = organization.status,
                createdAt = organization.createdAt,
                updatedAt = organization.updatedAt,
            )
        }
    }
}