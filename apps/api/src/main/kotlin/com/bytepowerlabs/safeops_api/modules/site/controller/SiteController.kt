package com.bytepowerlabs.safeops_api.modules.site.controller

import com.bytepowerlabs.safeops_api.modules.site.dto.CreateSiteRequest
import com.bytepowerlabs.safeops_api.modules.site.service.CreateSiteService
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
@RequestMapping("/organizations/{organizationId}/sites")
class SiteController(private val createSiteService: CreateSiteService) {
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createSite(
        @PathVariable organizationId: UUID,
        @Valid @RequestBody request: CreateSiteRequest,
        @AuthenticationPrincipal jwt: Jwt
    ) {
        createSiteService.execute(
            organizationId = organizationId,
            userAccountId = UUID.fromString(jwt.subject),
            request = request,
        )
    }
}