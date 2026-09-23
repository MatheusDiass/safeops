package com.bytepowerlabs.safeops_api.shared.realtime

import java.time.Instant
import java.util.UUID

data class RealtimeEvent(
    val eventId: UUID = UUID.randomUUID(),
    val eventType: RealtimeEventType,
    val organizationId: UUID,
    val siteId: UUID,
    val resourceId: UUID,
    val occurredAt: Instant = Instant.now(),
)
