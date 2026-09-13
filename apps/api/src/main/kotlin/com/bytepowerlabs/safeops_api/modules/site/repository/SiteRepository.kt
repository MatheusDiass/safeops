package com.bytepowerlabs.safeops_api.modules.site.repository

import com.bytepowerlabs.safeops_api.modules.site.entity.SiteEntity
import com.bytepowerlabs.safeops_api.modules.site.entity.SiteStatus
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.util.UUID

interface SiteRepository : JpaRepository<SiteEntity, UUID> {
    fun findByIdAndOrganizationId(id: UUID, organizationId: UUID): SiteEntity?
    fun findAllByOrganizationId(organizationId: UUID): List<SiteEntity>
    fun countByOrganizationIdAndStatus(organizationId: UUID, status: SiteStatus): Int

    @Query(
        """
        SELECT
            s.organization.id AS organizationId,
            COUNT(s.id) AS siteCount
        FROM SiteEntity s
        WHERE s.organization.id IN :organizationIds
          AND s.status = :status
        GROUP BY s.organization.id
        """
    )
    fun countSitesByOrganizationIdsAndStatus(
        organizationIds: List<UUID>,
        status: SiteStatus
    ): List<SiteCountByOrganizationProjection>
}
