package com.bytepowerlabs.safeops_api.modules.incident.dto

import com.bytepowerlabs.safeops_api.modules.incident.entity.IncidentStatus

data class UpdateIncidentStatusRequest(
    val status: IncidentStatus
)
