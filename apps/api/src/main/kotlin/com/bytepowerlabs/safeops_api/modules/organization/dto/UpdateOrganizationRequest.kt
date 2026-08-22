package com.bytepowerlabs.safeops_api.modules.organization.dto

import com.bytepowerlabs.safeops_api.modules.organization.entity.OrganizationStatus
import jakarta.validation.constraints.Size

data class UpdateOrganizationRequest(
    @Size(min = 3, max = 150, message = "Organization name must be between 3 and 150")
    val name: String? = null,

    val status: OrganizationStatus? = null
)
