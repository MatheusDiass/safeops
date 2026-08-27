package com.bytepowerlabs.safeops_api.modules.incident.controller

import com.bytepowerlabs.safeops_api.modules.incident.dto.CreateIncidentRequest
import com.bytepowerlabs.safeops_api.modules.incident.dto.IncidentResponse
import com.bytepowerlabs.safeops_api.modules.incident.service.CreateIncidentService
import com.bytepowerlabs.safeops_api.modules.incident.service.GetIncidentService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/organizations/{organizationId}/sites/{siteId}/incidents")
class IncidentController(
    private val createIncidentService: CreateIncidentService,
    private val getIncidentService: GetIncidentService
) {
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createIncident(
        @PathVariable organizationId: UUID,
        @PathVariable siteId: UUID,
        @Valid @RequestBody request: CreateIncidentRequest,
        @AuthenticationPrincipal jwt: Jwt
    ) {
        createIncidentService.execute(
            organizationId = organizationId,
            siteId = siteId,
            request = request,
            userAccountId = UUID.fromString(jwt.subject)
        )
    }

    @GetMapping("/{incidentId}")
    fun getIncident(
        @PathVariable organizationId: UUID,
        @PathVariable siteId: UUID,
        @PathVariable incidentId: UUID,
        @AuthenticationPrincipal jwt: Jwt
    ): IncidentResponse {
        return getIncidentService.execute(
            organizationId = organizationId,
            siteId = siteId,
            incidentId = incidentId,
            userAccountId = UUID.fromString(jwt.subject)
        )
    }
}