package com.bytepowerlabs.safeops_api.modules.organization.repository

import com.bytepowerlabs.safeops_api.modules.organization.entity.OrganizationMembershipEntity
import com.bytepowerlabs.safeops_api.modules.organization.entity.OrganizationMembershipStatus
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface OrganizationMembershipRepository : JpaRepository<OrganizationMembershipEntity, UUID> {
    fun findByOrganizationIdAndUserAccountId(organizationId: UUID, userAccountId: UUID): OrganizationMembershipEntity?
    fun findAllByUserAccountIdAndStatus(
        userAccountId: UUID,
        status: OrganizationMembershipStatus
    ): List<OrganizationMembershipEntity>
}