package com.bytepowerlabs.safeops_api.modules.incident.entity

enum class IncidentStatus {
    REPORTED,
    UNDER_REVIEW,
    ACTION_REQUIRED,
    CLOSED;

    fun canTransitionTo(newStatus: IncidentStatus): Boolean {
        return when (this) {
            REPORTED -> newStatus == UNDER_REVIEW
            UNDER_REVIEW -> newStatus == ACTION_REQUIRED || newStatus == CLOSED
            ACTION_REQUIRED ->  newStatus == UNDER_REVIEW
            CLOSED -> false
        }
    }
}