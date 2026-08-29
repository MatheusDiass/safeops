package com.bytepowerlabs.safeops_api.modules.incident.service

import com.bytepowerlabs.safeops_api.modules.incident.dto.UpdateIncidentRequest
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
class UpdateIncidentService(
    private val membershipRepository: OrganizationMembershipRepository,
    private val incidentRepository: IncidentRepository
) {
    @Transactional
    fun execute(
        organizationId: UUID,
        siteId: UUID,
        incidentId: UUID,
        userAccountId: UUID,
        request: UpdateIncidentRequest
    ) {
        val membership = membershipRepository.findByOrganizationIdAndUserAccountId(
            organizationId = organizationId,
            userAccountId = userAccountId
        ) ?: throw OrganizationNotFoundException()


        if (membership.status != OrganizationMembershipStatus.ACTIVE) {
            throw OrganizationAccessDeniedException()
        }

        val incident = incidentRepository.findByIdAndOrganizationIdAndSiteId(
            id = incidentId,
            organizationId = organizationId,
            siteId = siteId
        ) ?: throw IncidentNotFoundException()

        val isOwner = membership.role == OrganizationRole.OWNER
        val isReporter = incident.reportedBy.id == userAccountId

        if (!isOwner && !isReporter) {
            throw OrganizationAccessDeniedException()
        }

        incident.update(
            title = request.title?.trim(),
            description = request.description?.trim(),
            type = request.type,
            severity = request.severity,
            occurredAt = request.occurredAt,
            location = request.location?.trim(),
            immediateActions = request.immediateActions?.trim(),
        )
    }
}