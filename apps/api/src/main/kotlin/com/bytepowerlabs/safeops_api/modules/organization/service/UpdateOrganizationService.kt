package com.bytepowerlabs.safeops_api.modules.organization.service

import com.bytepowerlabs.safeops_api.modules.organization.dto.OrganizationResponse
import com.bytepowerlabs.safeops_api.modules.organization.dto.UpdateOrganizationRequest
import com.bytepowerlabs.safeops_api.modules.organization.entity.OrganizationRole
import com.bytepowerlabs.safeops_api.modules.organization.exception.OrganizationAccessDeniedException
import com.bytepowerlabs.safeops_api.modules.organization.exception.OrganizationNotFoundException
import com.bytepowerlabs.safeops_api.modules.organization.repository.OrganizationMembershipRepository
import com.bytepowerlabs.safeops_api.modules.site.entity.SiteStatus
import com.bytepowerlabs.safeops_api.modules.site.repository.SiteRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class UpdateOrganizationService(
    private val membershipRepository: OrganizationMembershipRepository,
    private val siteRepository: SiteRepository
) {
    @Transactional
    fun execute(organizationId: UUID, userAccountId: UUID, request: UpdateOrganizationRequest): OrganizationResponse {
        val membership = membershipRepository.findByOrganizationIdAndUserAccountId(organizationId, userAccountId)
            ?: throw OrganizationNotFoundException()

        if (membership.role != OrganizationRole.OWNER) {
            throw OrganizationAccessDeniedException()
        }

        val organization = membership.organization
        organization.update(name = request.name, status = request.status)

        val siteCount =
            siteRepository.countByOrganizationIdAndStatus(organizationId = organizationId, status = SiteStatus.ACTIVE)

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