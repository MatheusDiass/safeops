package com.bytepowerlabs.safeops_api.modules.identity.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import org.hibernate.validator.constraints.Length

data class CreateUserAccountRequest(
    @NotBlank(message = "Username cannot be blank")
    @Length(min = 3, max = 100, message = "Username must be between 3 and 100 characters")
    val name: String,

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Invalid email format")
    val email: String,

    @NotBlank(message = "Password cannot be blank")
    val password: String
)
