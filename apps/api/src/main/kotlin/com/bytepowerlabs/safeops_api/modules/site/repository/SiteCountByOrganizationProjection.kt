package com.bytepowerlabs.safeops_api.modules.site.repository

import java.util.UUID

interface SiteCountByOrganizationProjection {
    val organizationId: UUID
    val siteCount: Int
}