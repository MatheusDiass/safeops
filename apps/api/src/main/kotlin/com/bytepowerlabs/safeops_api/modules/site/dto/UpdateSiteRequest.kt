package com.bytepowerlabs.safeops_api.modules.site.dto

import com.bytepowerlabs.safeops_api.modules.site.entity.SiteStatus
import jakarta.validation.constraints.Size

data class UpdateSiteRequest(
    @Size(min = 3, max = 150, message = "Site name must be between 3 and 150")
    val name: String?,

    val status: SiteStatus?
)
