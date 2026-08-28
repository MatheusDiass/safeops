package com.bytepowerlabs.safeops_api.modules.incident.repository

import com.bytepowerlabs.safeops_api.modules.incident.entity.IncidentEntity
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface IncidentRepository : JpaRepository<IncidentEntity, UUID> {
    fun findByIdAndOrganizationIdAndSiteId(id: UUID, organizationId: UUID, siteId: UUID): IncidentEntity?

    @EntityGraph(attributePaths = ["reportedBy"])
    fun findAllByOrganizationIdAndSiteId(organizationId: UUID, siteId: UUID): List<IncidentEntity>
}