package com.bytepowerlabs.safeops_api.modules.incident.exception

import com.bytepowerlabs.safeops_api.shared.BaseException
import com.bytepowerlabs.safeops_api.shared.ErrorCategory

class IncidentOccurrenceDateInFutureException : BaseException(
    code = "INCIDENT_OCCURRENCE_DATE_IN_FUTURE",
    title = "Invalid incident occurrence date",
    message = "The incident occurrence date cannot be in the future.",
    errorCategory = ErrorCategory.INVALID_INPUT
)
