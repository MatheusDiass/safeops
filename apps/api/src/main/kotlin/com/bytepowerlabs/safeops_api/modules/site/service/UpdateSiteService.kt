package com.bytepowerlabs.safeops_api.modules.site.service

import com.bytepowerlabs.safeops_api.modules.organization.entity.OrganizationRole
import com.bytepowerlabs.safeops_api.modules.organization.exception.OrganizationAccessDeniedException
import com.bytepowerlabs.safeops_api.modules.organization.exception.OrganizationNotFoundException
import com.bytepowerlabs.safeops_api.modules.organization.repository.OrganizationMembershipRepository
import com.bytepowerlabs.safeops_api.modules.site.dto.UpdateSiteRequest
import com.bytepowerlabs.safeops_api.modules.site.exception.SiteNotFoundException
import com.bytepowerlabs.safeops_api.modules.site.repository.SiteRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class UpdateSiteService(
    private val membershipRepository: OrganizationMembershipRepository,
    private val siteRepository: SiteRepository
) {
    @Transactional
    fun execute(organizationId: UUID, siteId: UUID, userAccountId: UUID, request: UpdateSiteRequest) {
        val membership = membershipRepository.findByOrganizationIdAndUserAccountId(
            organizationId = organizationId,
            userAccountId = userAccountId
        ) ?: throw OrganizationNotFoundException()

        if (membership.role != OrganizationRole.OWNER) {
            throw OrganizationAccessDeniedException()
        }

        val site = siteRepository.findByIdAndOrganizationId(id = siteId, organizationId = organizationId)
            ?: throw SiteNotFoundException()

        site.update(name = request.name, status = request.status)
    }
}