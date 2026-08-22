package com.bytepowerlabs.safeops_api.modules.organization.service

import com.bytepowerlabs.safeops_api.modules.organization.dto.UpdateOrganizationRequest
import com.bytepowerlabs.safeops_api.modules.organization.entity.OrganizationRole
import com.bytepowerlabs.safeops_api.modules.organization.exception.OrganizationAccessDeniedException
import com.bytepowerlabs.safeops_api.modules.organization.exception.OrganizationNotFoundException
import com.bytepowerlabs.safeops_api.modules.organization.repository.OrganizationMembershipRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class UpdateOrganizationService(private val membershipRepository: OrganizationMembershipRepository) {
    @Transactional
    fun execute(organizationId: UUID, userAccountId: UUID, request: UpdateOrganizationRequest) {
        val membership = membershipRepository.findByOrganizationIdAndUserAccountId(organizationId, userAccountId)
            ?: throw OrganizationNotFoundException()

        if (membership.role != OrganizationRole.OWNER) {
            throw OrganizationAccessDeniedException()
        }

        val organization = membership.organization
        organization.update(name = request.name, status = request.status)
    }
}