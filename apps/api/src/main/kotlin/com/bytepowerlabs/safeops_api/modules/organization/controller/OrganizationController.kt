package com.bytepowerlabs.safeops_api.modules.organization.controller

import com.bytepowerlabs.safeops_api.modules.organization.dto.CreateOrganizationRequest
import com.bytepowerlabs.safeops_api.modules.organization.dto.OrganizationResponse
import com.bytepowerlabs.safeops_api.modules.organization.dto.UpdateOrganizationRequest
import com.bytepowerlabs.safeops_api.modules.organization.service.CreateOrganizationService
import com.bytepowerlabs.safeops_api.modules.organization.service.GetOrganizationService
import com.bytepowerlabs.safeops_api.modules.organization.service.ListOrganizationsService
import com.bytepowerlabs.safeops_api.modules.organization.service.SubscribeOrganizationEventsService
import com.bytepowerlabs.safeops_api.modules.organization.service.UpdateOrganizationService
import jakarta.validation.Valid
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter
import java.util.UUID

@RestController
@RequestMapping("/organizations")
class OrganizationController(
    private val createOrganizationService: CreateOrganizationService,
    private val getOrganizationService: GetOrganizationService,
    private val listOrganizationsService: ListOrganizationsService,
    private val updateOrganizationService: UpdateOrganizationService,
    private val subscribeOrganizationEventsService: SubscribeOrganizationEventsService
) {
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createOrganization(
        @Valid @RequestBody request: CreateOrganizationRequest,
        @AuthenticationPrincipal jwt: Jwt
    ): OrganizationResponse {
        return createOrganizationService.execute(
            request = request,
            userAccountId = UUID.fromString(jwt.subject)
        )
    }

    @GetMapping("/{organizationId}")
    fun getOrganization(
        @PathVariable organizationId: UUID,
        @AuthenticationPrincipal jwt: Jwt
    ): OrganizationResponse {
        return getOrganizationService.execute(
            organizationId = organizationId,
            userAccountId = UUID.fromString(jwt.subject)
        )
    }

    @GetMapping
    fun listOrganizations(@AuthenticationPrincipal jwt: Jwt): List<OrganizationResponse> {
        return listOrganizationsService.execute(UUID.fromString(jwt.subject))
    }

    @PatchMapping("/{organizationId}")
    fun updateOrganization(
        @PathVariable organizationId: UUID,
        @Valid @RequestBody request: UpdateOrganizationRequest,
        @AuthenticationPrincipal jwt: Jwt
    ): OrganizationResponse {
        return updateOrganizationService.execute(
            organizationId = organizationId,
            userAccountId = UUID.fromString(jwt.subject),
            request = request
        )
    }

    @GetMapping("/{organizationId}/events", produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
    fun subscribe(@PathVariable organizationId: UUID, @AuthenticationPrincipal jwt: Jwt): ResponseEntity<SseEmitter> {
        val accessTokenExpiresAt = requireNotNull(jwt.expiresAt) {
            "Access token expires at is missing"
        }

        val emitter = subscribeOrganizationEventsService.execute(
            organizationId = organizationId,
            userAccountId = UUID.fromString(jwt.subject),
            accessTokenExpiresAt = accessTokenExpiresAt
        )
        return ResponseEntity.ok().header(HttpHeaders.CACHE_CONTROL, "no-cache, no-transform")
            .header("X-Accel-Buffering", "no").body(emitter)
    }
}