package com.bytepowerlabs.safeops_api.modules.incident.repository

import com.bytepowerlabs.safeops_api.modules.incident.entity.IncidentEntity
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface IncidentRepository : JpaRepository<IncidentEntity, UUID> {
    @EntityGraph(attributePaths = ["reportedBy", "site"])
    fun findByIdAndOrganizationId(id: UUID, organizationId: UUID): IncidentEntity?

    @EntityGraph(attributePaths = ["reportedBy", "site"])
    fun findAllByOrganizationIdOrderById(organizationId: UUID): List<IncidentEntity>
}