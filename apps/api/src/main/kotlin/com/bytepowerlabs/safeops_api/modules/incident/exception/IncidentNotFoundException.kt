package com.bytepowerlabs.safeops_api.modules.incident.exception

import com.bytepowerlabs.safeops_api.shared.BaseException
import com.bytepowerlabs.safeops_api.shared.ErrorCategory

class IncidentNotFoundException : BaseException(
    code = "INCIDENT_NOT_FOUND",
    title = "Incident not found",
    message = "The requested incident was not found.",
    errorCategory = ErrorCategory.RESOURCE_NOT_FOUND
) {
}