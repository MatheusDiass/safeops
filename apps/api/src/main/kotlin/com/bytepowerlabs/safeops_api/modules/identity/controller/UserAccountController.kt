package com.bytepowerlabs.safeops_api.modules.identity.controller

import com.bytepowerlabs.safeops_api.modules.identity.dto.GetAuthenticatedUserAccountResponse
import com.bytepowerlabs.safeops_api.modules.identity.service.GetAuthenticatedUserAccountService
import org.springframework.http.HttpStatus
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/identity/me")
class UserAccountController(private val getAuthenticatedUserAccountService: GetAuthenticatedUserAccountService) {
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    fun me(@AuthenticationPrincipal jwt: Jwt): GetAuthenticatedUserAccountResponse {
        return getAuthenticatedUserAccountService.execute(UUID.fromString(jwt.subject))
    }
}