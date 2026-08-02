package com.bytepowerlabs.safeops_api.modules.identity.controller

import com.bytepowerlabs.safeops_api.modules.identity.dto.CreateUserAccountRequest
import com.bytepowerlabs.safeops_api.modules.identity.service.CreateUserAccountService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
class CreateUserAccountController(private val service: CreateUserAccountService) {
    @PostMapping("/identity/users")
    @ResponseStatus(HttpStatus.CREATED)
    fun handle(@Valid @RequestBody request: CreateUserAccountRequest) {
        service.execute(request)
    }
}
