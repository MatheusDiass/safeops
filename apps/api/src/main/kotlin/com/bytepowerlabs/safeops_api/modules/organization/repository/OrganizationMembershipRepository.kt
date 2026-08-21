package com.bytepowerlabs.safeops_api.modules.organization.repository

import com.bytepowerlabs.safeops_api.modules.organization.entity.OrganizationMembershipEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface OrganizationMembershipRepository : JpaRepository<OrganizationMembershipEntity, UUID> {
    fun findByOrganizationIdAndUserAccountId(organizationId: UUID, userAccountId: UUID): OrganizationMembershipEntity?
}