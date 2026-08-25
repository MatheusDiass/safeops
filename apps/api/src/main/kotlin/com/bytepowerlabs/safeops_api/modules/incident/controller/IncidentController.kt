package com.bytepowerlabs.safeops_api.modules.incident.controller

import com.bytepowerlabs.safeops_api.modules.incident.dto.CreateIncidentRequest
import com.bytepowerlabs.safeops_api.modules.incident.service.CreateIncidentService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/organizations/{organizationId}/sites/{siteId}/incidents")
class IncidentController(private val createIncidentService: CreateIncidentService) {
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
}