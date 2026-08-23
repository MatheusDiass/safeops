package com.bytepowerlabs.safeops_api.modules.site.service

import com.bytepowerlabs.safeops_api.modules.organization.exception.OrganizationNotFoundException
import com.bytepowerlabs.safeops_api.modules.organization.repository.OrganizationMembershipRepository
import com.bytepowerlabs.safeops_api.modules.site.dto.SiteResponse
import com.bytepowerlabs.safeops_api.modules.site.repository.SiteRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class ListSitesService(
    private val membershipRepository: OrganizationMembershipRepository,
    private val siteRepository: SiteRepository
) {
    @Transactional(readOnly = true)
    fun execute(organizationId: UUID, userAccountId: UUID): List<SiteResponse> {
        membershipRepository.findByOrganizationIdAndUserAccountId(
            organizationId = organizationId,
            userAccountId = userAccountId
        ) ?: throw OrganizationNotFoundException()

        val sites = siteRepository.findAllByOrganizationId(organizationId)

        return sites.map { site ->
            SiteResponse(
                id = site.id,
                name = site.name,
                status = site.status,
                createdAt = site.createdAt,
                updatedAt = site.updatedAt,
            )
        }
    }
}