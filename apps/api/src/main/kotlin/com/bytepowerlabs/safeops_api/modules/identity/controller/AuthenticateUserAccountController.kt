package com.bytepowerlabs.safeops_api.modules.identity.controller

import com.bytepowerlabs.safeops_api.modules.identity.dto.AuthenticateUserAccountRequest
import com.bytepowerlabs.safeops_api.modules.identity.dto.AuthenticateUserAccountResponse
import com.bytepowerlabs.safeops_api.modules.identity.service.AuthenticateUserAccountService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
class AuthenticateUserAccountController (private val service: AuthenticateUserAccountService) {
    @PostMapping("/identity/authenticate")
    @ResponseStatus(HttpStatus.OK)
    fun handle(@Valid @RequestBody request: AuthenticateUserAccountRequest): AuthenticateUserAccountResponse {
        return service.execute(request)
    }
}