package com.bytepowerlabs.safeops_api.shared.realtime

import com.fasterxml.jackson.annotation.JsonValue

enum class RealtimeEventType(val eventName: String) {
    INCIDENT_CREATED("incident.created"),
    INCIDENT_UPDATED("incident.updated"),
    INCIDENT_STATUS_UPDATED("incident.status-updated");

    @JsonValue
    fun jsonValue(): String = eventName
}