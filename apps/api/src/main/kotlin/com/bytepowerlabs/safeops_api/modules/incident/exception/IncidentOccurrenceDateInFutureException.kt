package com.bytepowerlabs.safeops_api.modules.incident.exception

class IncidentOccurrenceDateInFutureException : RuntimeException("Occurrence date cannot be in the future") {
}