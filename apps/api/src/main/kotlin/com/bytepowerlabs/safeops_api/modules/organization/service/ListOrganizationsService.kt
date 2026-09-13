package com.bytepowerlabs.safeops_api.modules.organization.service

import com.bytepowerlabs.safeops_api.modules.organization.dto.OrganizationResponse
import com.bytepowerlabs.safeops_api.modules.organization.entity.OrganizationMembershipStatus
import com.bytepowerlabs.safeops_api.modules.organization.repository.OrganizationMembershipRepository
import com.bytepowerlabs.safeops_api.modules.site.entity.SiteStatus
import com.bytepowerlabs.safeops_api.modules.site.repository.SiteRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class ListOrganizationsService(
    private val membershipRepository: OrganizationMembershipRepository,
    private val siteRepository: SiteRepository,
) {
    @Transactional(readOnly = true)
    fun execute(userAccountId: UUID): List<OrganizationResponse> {
        val memberships = membershipRepository.findAllByUserAccountIdAndStatus(
            userAccountId = userAccountId,
            status = OrganizationMembershipStatus.ACTIVE
        )

        if (memberships.isEmpty()) {
            return emptyList()
        }

        val organizationIds = memberships.map { it.organization.id }
        val siteCounts = siteRepository
            .countSitesByOrganizationIdsAndStatus(organizationIds, status = SiteStatus.ACTIVE)
            .associate { it.organizationId to it.siteCount }

        return memberships.map { membership ->
            val organization = membership.organization
            OrganizationResponse(
                id = organization.id,
                name = organization.name,
                status = organization.status,
                siteCount = siteCounts[organization.id] ?: 0,
                createdAt = organization.createdAt,
                updatedAt = organization.updatedAt,
            )
        }
    }
}
