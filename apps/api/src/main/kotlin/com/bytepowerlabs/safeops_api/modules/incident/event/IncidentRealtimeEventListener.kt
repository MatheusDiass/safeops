package com.bytepowerlabs.safeops_api.modules.incident.event

import com.bytepowerlabs.safeops_api.shared.realtime.OrganizationEventStream
import com.bytepowerlabs.safeops_api.shared.realtime.RealtimeEvent
import com.bytepowerlabs.safeops_api.shared.realtime.RealtimeEventType
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener

@Component
class IncidentRealtimeEventListener(private val organizationEventStream: OrganizationEventStream) {
    @Async("realtimeEventExecutor")
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    fun handle(event: IncidentCreatedApplicationEvent) {
        organizationEventStream.publish(RealtimeEvent(
            eventType = RealtimeEventType.INCIDENT_CREATED,
            organizationId = event.organizationId,
            siteId = event.siteId,
            resourceId = event.incidentId
        ))
    }
}