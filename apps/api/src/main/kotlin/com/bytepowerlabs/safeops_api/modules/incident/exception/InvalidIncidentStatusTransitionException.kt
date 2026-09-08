package com.bytepowerlabs.safeops_api.modules.incident.exception

import com.bytepowerlabs.safeops_api.modules.incident.entity.IncidentStatus
import com.bytepowerlabs.safeops_api.shared.BaseException
import com.bytepowerlabs.safeops_api.shared.ErrorCategory

class InvalidIncidentStatusTransitionException(
    private val currentStatus: IncidentStatus,
    private val newStatus: IncidentStatus
) : BaseException(
    code = "INVALID_INCIDENT_STATUS_TRANSITION",
    title = "Invalid incident status transition",
    message = "Incident status cannot transition from $currentStatus to $newStatus.",
    errorCategory = ErrorCategory.INVALID_INPUT
)
