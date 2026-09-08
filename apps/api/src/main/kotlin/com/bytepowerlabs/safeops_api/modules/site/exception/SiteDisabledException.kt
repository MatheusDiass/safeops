package com.bytepowerlabs.safeops_api.modules.site.exception

import com.bytepowerlabs.safeops_api.shared.BaseException
import com.bytepowerlabs.safeops_api.shared.ErrorCategory

class SiteDisabledException : BaseException(
    code = "SITE_DISABLED",
    title = "Site disabled",
    message = "The site is disabled.",
    errorCategory = ErrorCategory.ACCESS_DENIED
)
