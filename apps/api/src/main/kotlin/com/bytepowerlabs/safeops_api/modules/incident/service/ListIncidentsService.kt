package com.bytepowerlabs.safeops_api.modules.incident.service

import com.bytepowerlabs.safeops_api.modules.incident.dto.IncidentReporterResponse
import com.bytepowerlabs.safeops_api.modules.incident.dto.IncidentResponse
import com.bytepowerlabs.safeops_api.modules.incident.dto.IncidentSiteResponse
import com.bytepowerlabs.safeops_api.modules.incident.repository.IncidentRepository
import com.bytepowerlabs.safeops_api.modules.organization.entity.OrganizationMembershipStatus
import com.bytepowerlabs.safeops_api.modules.organization.exception.OrganizationAccessDeniedException
import com.bytepowerlabs.safeops_api.modules.organization.exception.OrganizationNotFoundException
import com.bytepowerlabs.safeops_api.modules.organization.repository.OrganizationMembershipRepository
import com.bytepowerlabs.safeops_api.modules.site.exception.SiteNotFoundException
import com.bytepowerlabs.safeops_api.modules.site.repository.SiteRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class ListIncidentsService(
    private val membershipRepository: OrganizationMembershipRepository,
    private val incidentRepository: IncidentRepository
) {
    @Transactional(readOnly = true)
    fun execute(organizationId: UUID, userAccountId: UUID): List<IncidentResponse> {
        val membership = membershipRepository.findByOrganizationIdAndUserAccountId(
            organizationId = organizationId,
            userAccountId = userAccountId
        ) ?: throw OrganizationNotFoundException()

        if (membership.status != OrganizationMembershipStatus.ACTIVE) {
            throw OrganizationAccessDeniedException()
        }

        val incidents =
            incidentRepository.findAllByOrganizationIdOrderById(organizationId = organizationId)

        return incidents.map { incident ->
            IncidentResponse(
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
                site = IncidentSiteResponse(
                    id = incident.site.id,
                    name = incident.site.name,
                ),
                closedAt = incident.closedAt,
                createdAt = incident.createdAt,
                updatedAt = incident.updatedAt,
            )
        }
    }
}