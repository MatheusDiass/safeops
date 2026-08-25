package com.bytepowerlabs.safeops_api.modules.incident.dto

import com.bytepowerlabs.safeops_api.modules.incident.entity.IncidentSeverity
import com.bytepowerlabs.safeops_api.modules.incident.entity.IncidentType
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.PastOrPresent
import jakarta.validation.constraints.Size
import java.time.Instant

data class CreateIncidentRequest(
    @NotBlank(message = "Title cannot be blank")
    @Size(max = 150, message = "Title must be at most 150 characters")
    val title: String,

    @NotBlank(message = "Description cannot be blank")
    @Size(max = 3000, message = "Description must be at most 5000 characters")
    val description: String,

    val type: IncidentType,

    val severity: IncidentSeverity? = null,

    @PastOrPresent(message = "Occurrence date cannot be in the future")
    val occurredAt: Instant,

    @Size(max = 150, message = "Location must be at most 150 characters")
    val location: String? = null,

    @Size(max = 5000, message = "Immediate actions must be at most 3000 characters")
    val immediateActions: String? = null,
)
