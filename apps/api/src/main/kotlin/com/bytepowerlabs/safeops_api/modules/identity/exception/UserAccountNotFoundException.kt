package com.bytepowerlabs.safeops_api.modules.identity.exception

import com.bytepowerlabs.safeops_api.shared.BaseException
import com.bytepowerlabs.safeops_api.shared.ErrorCategory

class UserAccountNotFoundException : BaseException(
    code = "USER_ACCOUNT_NOT_FOUND",
    title = "User account not found",
    message = "The requested user account was not found.",
    errorCategory = ErrorCategory.RESOURCE_NOT_FOUND
)
