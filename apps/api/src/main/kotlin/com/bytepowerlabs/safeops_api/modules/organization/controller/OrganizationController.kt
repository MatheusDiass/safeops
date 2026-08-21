package com.bytepowerlabs.safeops_api.modules.organization.controller

import com.bytepowerlabs.safeops_api.modules.organization.dto.CreateOrganizationRequest
import com.bytepowerlabs.safeops_api.modules.organization.dto.OrganizationResponse
import com.bytepowerlabs.safeops_api.modules.organization.service.CreateOrganizationService
import com.bytepowerlabs.safeops_api.modules.organization.service.GetOrganizationService
import com.bytepowerlabs.safeops_api.modules.organization.service.ListOrganizationsService
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
@RequestMapping("/organizations")
class OrganizationController(
    private val createOrganizationService: CreateOrganizationService,
    private val getOrganizationService: GetOrganizationService,
    private val listOrganizationsService: ListOrganizationsService
) {
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createOrganization(@Valid @RequestBody request: CreateOrganizationRequest, @AuthenticationPrincipal jwt: Jwt) {
        createOrganizationService.execute(request = request, userAccountId = UUID.fromString(jwt.subject))
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
}