package com.bytepowerlabs.safeops_api.modules.site.service

import com.bytepowerlabs.safeops_api.modules.organization.exception.OrganizationNotFoundException
import com.bytepowerlabs.safeops_api.modules.organization.repository.OrganizationMembershipRepository
import com.bytepowerlabs.safeops_api.modules.site.dto.SiteResponse
import com.bytepowerlabs.safeops_api.modules.site.exception.SiteNotFoundException
import com.bytepowerlabs.safeops_api.modules.site.repository.SiteRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class GetSiteService(
    private val membershipRepository: OrganizationMembershipRepository,
    private val siteRepository: SiteRepository
) {
    @Transactional(readOnly = true)
    fun execute(organizationId: UUID, siteId: UUID, userAccountId: UUID): SiteResponse {
        membershipRepository.findByOrganizationIdAndUserAccountId(
            organizationId = organizationId,
            userAccountId = userAccountId
        ) ?: throw OrganizationNotFoundException()

        val site = siteRepository.findByIdAndOrganizationId(id = siteId, organizationId = organizationId)
            ?: throw SiteNotFoundException()

        return SiteResponse(
            id = site.id,
            name = site.name,
            status = site.status,
            createdAt = site.createdAt,
            updatedAt = site.updatedAt
        )
    }
}