package com.bytepowerlabs.safeops_api.modules.site.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class CreateSiteRequest(
    @NotBlank(message = "Site name cannot be blank")
    @Size(min = 3, max = 150, message = "Site name must be between 3 and 150")
    val name: String,
)
