package com.bytepowerlabs.safeops_api.modules.identity.exception

import com.bytepowerlabs.safeops_api.shared.BaseException
import com.bytepowerlabs.safeops_api.shared.ErrorCategory

class InvalidRefreshTokenException : BaseException(
    code = "INVALID_REFRESH_TOKEN",
    title = "Invalid refresh token",
    message = "The refresh token is invalid or expired.",
    errorCategory = ErrorCategory.UNAUTHORIZED
)
