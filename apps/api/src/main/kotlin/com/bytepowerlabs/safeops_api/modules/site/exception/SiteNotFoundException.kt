package com.bytepowerlabs.safeops_api.modules.site.exception

import com.bytepowerlabs.safeops_api.shared.BaseException
import com.bytepowerlabs.safeops_api.shared.ErrorCategory

class SiteNotFoundException : BaseException(
    code = "SITE_NOT_FOUND",
    title = "Site not found",
    message = "The requested site was not found.",
    errorCategory = ErrorCategory.RESOURCE_NOT_FOUND
)
