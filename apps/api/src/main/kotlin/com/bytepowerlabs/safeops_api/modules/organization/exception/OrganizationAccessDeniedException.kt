package com.bytepowerlabs.safeops_api.modules.organization.exception

import com.bytepowerlabs.safeops_api.shared.BaseException
import com.bytepowerlabs.safeops_api.shared.ErrorCategory

class OrganizationAccessDeniedException : BaseException(
    code = "ORGANIZATION_ACCESS_DENIED",
    title = "Organization access denied",
    message = "You do not have permission to access this organization.",
    errorCategory = ErrorCategory.ACCESS_DENIED
)
