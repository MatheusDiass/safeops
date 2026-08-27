package com.bytepowerlabs.safeops_api.modules.incident.dto

import com.bytepowerlabs.safeops_api.modules.incident.entity.IncidentSeverity
import com.bytepowerlabs.safeops_api.modules.incident.entity.IncidentStatus
import com.bytepowerlabs.safeops_api.modules.incident.entity.IncidentType
import java.time.Instant
import java.util.UUID

data class IncidentResponse(
    val id: UUID,
    val title: String,
    val description: String,
    val type: IncidentType,
    val status: IncidentStatus,
    val severity: IncidentSeverity?,
    val occurredAt: Instant,
    val location: String?,
    val immediateActions: String?,
    val reportedBy: IncidentReporterResponse,
    val closedAt: Instant?,
    val createdAt: Instant,
    val updatedAt: Instant,
)

data class IncidentReporterResponse(
    val id: UUID,
    val name: String,
)
