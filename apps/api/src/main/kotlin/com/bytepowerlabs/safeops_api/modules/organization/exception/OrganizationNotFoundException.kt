package com.bytepowerlabs.safeops_api.modules.organization.exception

import com.bytepowerlabs.safeops_api.shared.BaseException
import com.bytepowerlabs.safeops_api.shared.ErrorCategory

class OrganizationNotFoundException : BaseException(
    code = "ORGANIZATION_NOT_FOUND",
    title = "Organization not found",
    message = "The requested organization was not found.",
    errorCategory = ErrorCategory.RESOURCE_NOT_FOUND
) {
}