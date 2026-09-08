package com.bytepowerlabs.safeops_api.modules.identity.exception

import com.bytepowerlabs.safeops_api.shared.BaseException
import com.bytepowerlabs.safeops_api.shared.ErrorCategory

class UserAccountUnavailableException : BaseException(
    code = "USER_ACCOUNT_UNAVAILABLE",
    title = "User account unavailable",
    message = "The user account is unavailable.",
    errorCategory = ErrorCategory.ACCESS_DENIED
)
