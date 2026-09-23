package com.bytepowerlabs.safeops_api.shared.realtime

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter
import java.util.UUID

data class SseConnection(
    val id: UUID,
    val organizationId: UUID,
    val userAccountId: UUID,
    val emitter: SseEmitter
)
