package com.bytepowerlabs.safeops_api.modules.organization.service

import com.bytepowerlabs.safeops_api.modules.organization.entity.OrganizationMembershipStatus
import com.bytepowerlabs.safeops_api.modules.organization.exception.OrganizationAccessDeniedException
import com.bytepowerlabs.safeops_api.modules.organization.exception.OrganizationNotFoundException
import com.bytepowerlabs.safeops_api.modules.organization.repository.OrganizationMembershipRepository
import com.bytepowerlabs.safeops_api.shared.realtime.OrganizationEventStream
import org.springframework.stereotype.Service
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter
import java.time.Duration
import java.time.Instant
import java.util.UUID

@Service
class SubscribeOrganizationEventsService(
    private val membershipRepository: OrganizationMembershipRepository,
    private val organizationEventStream: OrganizationEventStream
) {
    fun execute(organizationId: UUID, userAccountId: UUID, accessTokenExpiresAt: Instant): SseEmitter {
        val membership = membershipRepository.findByOrganizationIdAndUserAccountId(
            organizationId = organizationId,
            userAccountId = userAccountId
        ) ?: throw OrganizationNotFoundException()

        if (membership.status != OrganizationMembershipStatus.ACTIVE) {
            throw OrganizationAccessDeniedException()
        }

        val timeoutMillis = Duration.between(
            Instant.now(),
            accessTokenExpiresAt
        ).toMillis()

        return organizationEventStream.subscribe(organizationId = organizationId, timeoutMillis = timeoutMillis)
    }
}