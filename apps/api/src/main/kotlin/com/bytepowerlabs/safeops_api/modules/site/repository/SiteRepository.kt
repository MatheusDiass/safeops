package com.bytepowerlabs.safeops_api.modules.site.repository

import com.bytepowerlabs.safeops_api.modules.site.entity.SiteEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface SiteRepository : JpaRepository<SiteEntity, UUID> {
}