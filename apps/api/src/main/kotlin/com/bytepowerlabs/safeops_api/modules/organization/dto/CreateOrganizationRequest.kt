package com.bytepowerlabs.safeops_api.modules.organization.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class CreateOrganizationRequest(
    @NotBlank(message = "Organization name cannot be blank")
    @Size(min = 3, max = 150, message = "Organization name must be between 3 and 150")
    val name: String,
)
