package com.bytepowerlabs.safeops_api.modules.site.controller

import com.bytepowerlabs.safeops_api.modules.site.dto.CreateSiteRequest
import com.bytepowerlabs.safeops_api.modules.site.dto.SiteResponse
import com.bytepowerlabs.safeops_api.modules.site.dto.UpdateSiteRequest
import com.bytepowerlabs.safeops_api.modules.site.service.CreateSiteService
import com.bytepowerlabs.safeops_api.modules.site.service.GetSiteService
import com.bytepowerlabs.safeops_api.modules.site.service.ListSitesService
import com.bytepowerlabs.safeops_api.modules.site.service.UpdateSiteService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
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
import java.util.UUID

@RestController
@RequestMapping("/organizations/{organizationId}/sites")
class SiteController(
    private val createSiteService: CreateSiteService,
    private val getSiteService: GetSiteService,
    private val listSitesService: ListSitesService,
    private val updateSiteService: UpdateSiteService,
) {
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

    @GetMapping("/{siteId}")
    fun getSite(
        @PathVariable organizationId: UUID,
        @PathVariable siteId: UUID,
        @AuthenticationPrincipal jwt: Jwt
    ): SiteResponse {
        return getSiteService.execute(
            organizationId = organizationId,
            siteId = siteId,
            userAccountId = UUID.fromString(jwt.subject)
        )
    }

    @GetMapping
    fun listSites(@PathVariable organizationId: UUID, @AuthenticationPrincipal jwt: Jwt): List<SiteResponse> {
        return listSitesService.execute(organizationId = organizationId, userAccountId = UUID.fromString(jwt.subject))
    }

    @PatchMapping("/{siteId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun updateSite(
        @PathVariable organizationId: UUID,
        @PathVariable siteId: UUID,
        @Valid @RequestBody request: UpdateSiteRequest,
        @AuthenticationPrincipal jwt: Jwt
    ) {
        updateSiteService.execute(
            organizationId = organizationId,
            siteId = siteId,
            request = request,
            userAccountId = UUID.fromString(jwt.subject)
        )
    }
}