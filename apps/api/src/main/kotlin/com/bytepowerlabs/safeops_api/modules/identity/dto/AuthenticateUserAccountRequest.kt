package com.bytepowerlabs.safeops_api.modules.identity.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank

data class AuthenticateUserAccountRequest (
    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Invalid email format")
    val email: String,

    @NotBlank(message = "Password cannot be blank")
    val password: String
)