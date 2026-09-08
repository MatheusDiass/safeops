package com.bytepowerlabs.safeops_api.modules.identity.exception

import com.bytepowerlabs.safeops_api.shared.BaseException
import com.bytepowerlabs.safeops_api.shared.ErrorCategory

class EmailAlreadyExistsException : BaseException(
    code = "EMAIL_ALREADY_EXISTS",
    title = "Email already exists",
    message = "A user account with this email already exists.",
    errorCategory = ErrorCategory.CONFLICT
)
