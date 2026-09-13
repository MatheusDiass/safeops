package com.bytepowerlabs.safeops_api.modules.organization.service

import com.bytepowerlabs.safeops_api.modules.organization.dto.OrganizationResponse
import com.bytepowerlabs.safeops_api.modules.organization.exception.OrganizationNotFoundException
import com.bytepowerlabs.safeops_api.modules.organization.repository.OrganizationMembershipRepository
import com.bytepowerlabs.safeops_api.modules.site.entity.SiteStatus
import com.bytepowerlabs.safeops_api.modules.site.repository.SiteRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class GetOrganizationService(
    private val membershipRepository: OrganizationMembershipRepository,
    private val siteRepository: SiteRepository,
) {
    @Transactional(readOnly = true)
    fun execute(organizationId: UUID, userAccountId: UUID): OrganizationResponse {
        val membership =
            membershipRepository.findByOrganizationIdAndUserAccountId(organizationId, userAccountId)
                ?: throw OrganizationNotFoundException()

        val organization = membership.organization
        val siteCount = siteRepository.countByOrganizationIdAndStatus(
            organizationId = organizationId,
            status = SiteStatus.ACTIVE
        )

        return OrganizationResponse(
            id = organization.id,
            name = organization.name,
            status = organization.status,
            siteCount = siteCount,
            createdAt = organization.createdAt,
            updatedAt = organization.updatedAt,
        )
    }
}
