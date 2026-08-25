package com.bytepowerlabs.safeops_api.modules.incident.service

import com.bytepowerlabs.safeops_api.modules.incident.IncidentOccurrenceDateInFutureException
import com.bytepowerlabs.safeops_api.modules.incident.dto.CreateIncidentRequest
import com.bytepowerlabs.safeops_api.modules.incident.entity.IncidentEntity
import com.bytepowerlabs.safeops_api.modules.incident.repository.IncidentRepository
import com.bytepowerlabs.safeops_api.modules.organization.entity.OrganizationMembershipStatus
import com.bytepowerlabs.safeops_api.modules.organization.entity.OrganizationStatus
import com.bytepowerlabs.safeops_api.modules.organization.exception.OrganizationAccessDeniedException
import com.bytepowerlabs.safeops_api.modules.organization.exception.OrganizationDisabledException
import com.bytepowerlabs.safeops_api.modules.organization.exception.OrganizationNotFoundException
import com.bytepowerlabs.safeops_api.modules.organization.repository.OrganizationMembershipRepository
import com.bytepowerlabs.safeops_api.modules.site.entity.SiteStatus
import com.bytepowerlabs.safeops_api.modules.site.exception.SiteDisabledException
import com.bytepowerlabs.safeops_api.modules.site.exception.SiteNotFoundException
import com.bytepowerlabs.safeops_api.modules.site.repository.SiteRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.util.UUID

@Service
class CreateIncidentService(
    private val membershipRepository: OrganizationMembershipRepository,
    private val siteRepository: SiteRepository,
    private val incidentRepository: IncidentRepository
) {
    @Transactional
    fun execute(organizationId: UUID, siteId: UUID, userAccountId: UUID, request: CreateIncidentRequest) {
        val membership = membershipRepository.findByOrganizationIdAndUserAccountId(
            organizationId = organizationId,
            userAccountId = userAccountId
        ) ?: throw OrganizationNotFoundException()

        if (membership.status != OrganizationMembershipStatus.ACTIVE) {
            throw OrganizationAccessDeniedException()
        }

        val organization = membership.organization

        if (organization.status != OrganizationStatus.ACTIVE) {
            throw OrganizationDisabledException()
        }

        val site = siteRepository.findByIdAndOrganizationId(id = siteId, organizationId = organizationId)
            ?: throw SiteNotFoundException()

        if (site.status != SiteStatus.ACTIVE) {
            throw SiteDisabledException()
        }

        if (request.occurredAt.isAfter(Instant.now())) {
            throw IncidentOccurrenceDateInFutureException()
        }

        val incident = IncidentEntity(
            organization = organization,
            site = site,
            reportedBy = membership.userAccount,
            title = request.title,
            description = request.description,
            type = request.type,
            severity = request.severity,
            occurredAt = request.occurredAt,
            location = request.location?.trim()?.takeIf { it.isNotEmpty() },
            immediateActions = request.immediateActions?.trim()?.takeIf { it.isNotEmpty() },
        )

        incidentRepository.save(incident)
    }
}