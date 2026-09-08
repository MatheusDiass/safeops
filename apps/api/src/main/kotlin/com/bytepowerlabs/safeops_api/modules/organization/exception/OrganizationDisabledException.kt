package com.bytepowerlabs.safeops_api.modules.organization.exception

import com.bytepowerlabs.safeops_api.shared.BaseException
import com.bytepowerlabs.safeops_api.shared.ErrorCategory

class OrganizationDisabledException : BaseException(
    code = "ORGANIZATION_DISABLED",
    title = "Organization disabled",
    message = "The organization is disabled.",
    errorCategory = ErrorCategory.ACCESS_DENIED
)
