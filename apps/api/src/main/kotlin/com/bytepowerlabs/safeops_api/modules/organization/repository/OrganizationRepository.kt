package com.bytepowerlabs.safeops_api.modules.organization.repository

import com.bytepowerlabs.safeops_api.modules.organization.entity.OrganizationEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface OrganizationRepository : JpaRepository<OrganizationEntity, UUID> {
}