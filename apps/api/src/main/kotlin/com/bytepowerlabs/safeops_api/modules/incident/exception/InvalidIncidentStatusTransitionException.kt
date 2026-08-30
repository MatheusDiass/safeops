package com.bytepowerlabs.safeops_api.modules.incident.exception

import com.bytepowerlabs.safeops_api.modules.incident.entity.IncidentStatus

class InvalidIncidentStatusTransitionException(
    private val currentStatus: IncidentStatus,
    private val newStatus: IncidentStatus
) : RuntimeException("Incident status cannot transition from $currentStatus to $newStatus") {}