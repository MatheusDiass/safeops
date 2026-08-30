package com.bytepowerlabs.safeops_api.modules.incident.service

import com.bytepowerlabs.safeops_api.modules.incident.dto.UpdateIncidentStatusRequest
import com.bytepowerlabs.safeops_api.modules.incident.exception.IncidentNotFoundException
import com.bytepowerlabs.safeops_api.modules.incident.repository.IncidentRepository
import com.bytepowerlabs.safeops_api.modules.organization.entity.OrganizationMembershipStatus
import com.bytepowerlabs.safeops_api.modules.organization.entity.OrganizationRole
import com.bytepowerlabs.safeops_api.modules.organization.exception.OrganizationAccessDeniedException
import com.bytepowerlabs.safeops_api.modules.organization.exception.OrganizationNotFoundException
import com.bytepowerlabs.safeops_api.modules.organization.repository.OrganizationMembershipRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class UpdateIncidentStatusService(
    private val membershipRepository: OrganizationMembershipRepository,
    private val incidentRepository: IncidentRepository
) {
    @Transactional
    fun execute(
        organizationId: UUID,
        siteId: UUID,
        incidentId: UUID,
        userAccountId: UUID,
        request: UpdateIncidentStatusRequest
    ) {
        val membership = membershipRepository.findByOrganizationIdAndUserAccountId(
            organizationId= organizationId,
            userAccountId = userAccountId
        ) ?: throw OrganizationNotFoundException()

        if (membership.status != OrganizationMembershipStatus.ACTIVE || membership.role != OrganizationRole.OWNER) {
            throw OrganizationAccessDeniedException()
        }

        val incident = incidentRepository.findByIdAndOrganizationIdAndSiteId(
            id = incidentId,
            organizationId = organizationId,
            siteId = siteId
        ) ?: throw IncidentNotFoundException()

        incident.updateStatus(request.status)
    }
}