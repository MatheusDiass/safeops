package com.bytepowerlabs.safeops_api.modules.incident.event

import java.time.Instant
import java.util.UUID

data class IncidentCreatedApplicationEvent(
    val organizationId: UUID,
    val siteId: UUID,
    val incidentId: UUID,
    val occurredAt: Instant = Instant.now(),
)
