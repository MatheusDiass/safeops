package com.bytepowerlabs.safeops_api.modules.site.dto

import com.bytepowerlabs.safeops_api.modules.site.entity.SiteStatus
import java.time.Instant
import java.util.UUID

data class SiteResponse(
    val id: UUID,
    val name: String,
    val status: SiteStatus,
    val createdAt: Instant,
    val updatedAt: Instant?,
)
