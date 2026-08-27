package com.bytepowerlabs.safeops_api.modules.incident.service

import com.bytepowerlabs.safeops_api.modules.incident.dto.IncidentReporterResponse
import com.bytepowerlabs.safeops_api.modules.incident.dto.IncidentResponse
import com.bytepowerlabs.safeops_api.modules.incident.exception.IncidentNotFoundException
import com.bytepowerlabs.safeops_api.modules.incident.repository.IncidentRepository
import com.bytepowerlabs.safeops_api.modules.organization.entity.OrganizationMembershipStatus
import com.bytepowerlabs.safeops_api.modules.organization.exception.OrganizationAccessDeniedException
import com.bytepowerlabs.safeops_api.modules.organization.exception.OrganizationNotFoundException
import com.bytepowerlabs.safeops_api.modules.organization.repository.OrganizationMembershipRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class GetIncidentService(
    private val membershipRepository: OrganizationMembershipRepository,
    private val incidentRepository: IncidentRepository
) {
    @Transactional(readOnly = true)
    fun execute(organizationId: UUID, siteId: UUID, incidentId: UUID, userAccountId: UUID): IncidentResponse {
        val membership = membershipRepository.findByOrganizationIdAndUserAccountId(
            organizationId = organizationId,
            userAccountId = userAccountId
        ) ?: throw OrganizationNotFoundException()

        if(membership.status != OrganizationMembershipStatus.ACTIVE) {
            throw OrganizationAccessDeniedException()
        }

        val incident = incidentRepository.findByIdAndOrganizationIdAndSiteId(
            id = incidentId,
            organizationId = organizationId,
            siteId = siteId
        ) ?: throw IncidentNotFoundException()

        return IncidentResponse(
            id = incident.id,
            title = incident.title,
            description = incident.description,
            type = incident.type,
            status = incident.status,
            severity = incident.severity,
            occurredAt = incident.occurredAt,
            location = incident.location,
            immediateActions = incident.immediateActions,
            reportedBy = IncidentReporterResponse(
                id = incident.reportedBy.id,
                name = incident.reportedBy.name,
            ),
            closedAt = incident.closedAt,
            createdAt = incident.createdAt,
            updatedAt = incident.updatedAt,
        )
    }
}