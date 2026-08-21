package com.bytepowerlabs.safeops_api.modules.organization.dto

import com.bytepowerlabs.safeops_api.modules.organization.entity.OrganizationStatus
import java.time.Instant
import java.util.UUID

data class GetOrganizationResponse(
    val id: UUID,
    val name: String,
    val status: OrganizationStatus,
    val createdAt: Instant,
    val updatedAt: Instant? = null,
)
