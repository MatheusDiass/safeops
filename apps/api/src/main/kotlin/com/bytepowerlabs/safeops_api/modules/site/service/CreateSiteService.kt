package com.bytepowerlabs.safeops_api.modules.site.service

import com.bytepowerlabs.safeops_api.modules.organization.entity.OrganizationRole
import com.bytepowerlabs.safeops_api.modules.organization.exception.OrganizationAccessDeniedException
import com.bytepowerlabs.safeops_api.modules.organization.exception.OrganizationNotFoundException
import com.bytepowerlabs.safeops_api.modules.organization.repository.OrganizationMembershipRepository
import com.bytepowerlabs.safeops_api.modules.site.dto.CreateSiteRequest
import com.bytepowerlabs.safeops_api.modules.site.entity.SiteEntity
import com.bytepowerlabs.safeops_api.modules.site.repository.SiteRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class CreateSiteService(
    private val membershipRepository: OrganizationMembershipRepository,
    private val siteRepository: SiteRepository
) {
    @Transactional
    fun execute(organizationId: UUID, userAccountId: UUID, request: CreateSiteRequest) {
        val membership = membershipRepository.findByOrganizationIdAndUserAccountId(
            organizationId = organizationId,
            userAccountId = userAccountId
        ) ?: throw OrganizationNotFoundException()

        if (membership.role != OrganizationRole.OWNER) {
            throw OrganizationAccessDeniedException()
        }

        val organization = membership.organization
        val site = SiteEntity(name = request.name, organization = organization)

        siteRepository.save(site)
    }
}