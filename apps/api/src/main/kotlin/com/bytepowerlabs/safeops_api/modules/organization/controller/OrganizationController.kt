package com.bytepowerlabs.safeops_api.modules.organization.controller

import com.bytepowerlabs.safeops_api.modules.organization.dto.CreateOrganizationRequest
import com.bytepowerlabs.safeops_api.modules.organization.service.CreateOrganizationService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/organizations")
class OrganizationController(private val createOrganizationService: CreateOrganizationService) {
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createOrganization(@Valid @RequestBody request: CreateOrganizationRequest, @AuthenticationPrincipal jwt: Jwt) {
        createOrganizationService.execute(request = request, userAccountId = UUID.fromString(jwt.subject))
    }
}