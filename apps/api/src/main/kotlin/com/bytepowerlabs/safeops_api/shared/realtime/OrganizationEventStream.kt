package com.bytepowerlabs.safeops_api.shared.realtime

import org.springframework.http.MediaType
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

@Component
class OrganizationEventStream {
    private val emitters = ConcurrentHashMap<UUID, ConcurrentHashMap<UUID, SseEmitter>>()

    fun subscribe(organizationId: UUID, timeoutMillis: Long): SseEmitter {
        val connectionId = UUID.randomUUID()

        val emitter = SseEmitter(
            timeoutMillis.coerceIn(
                MIN_CONNECTION_TIMEOUT,
                MAX_CONNECTION_TIMEOUT
            )
        )

        emitters.compute(organizationId) { _, organizationEmitters ->
            (
                    organizationEmitters ?: ConcurrentHashMap()
                    ).also {
                    it[connectionId] = emitter
                }
        }

        configureLifecycle(organizationId = organizationId, connectionId = connectionId, emitter = emitter)

        try {
            emitter.send(
                SseEmitter.event().name("connected").reconnectTime(RECONNECT_TIME).data(
                    mapOf(
                        "connectionId" to connectionId
                    ),
                    MediaType.APPLICATION_JSON
                )
            )
        } catch (e: Exception) {
            remove(organizationId = organizationId, connectionId = connectionId)
            throw e
        }

        return emitter
    }

    fun publish(event: RealtimeEvent) {
        val organizationEmitters = emitters[event.organizationId] ?: return

        organizationEmitters.forEach { (connectionId, emitter) ->
            try {
                emitter.send(
                    SseEmitter.event().id(event.eventId.toString()).name(event.eventType.toString()).data(
                        event,
                        MediaType.APPLICATION_JSON
                    )
                )
            } catch (_: Exception) {
                remove(event.organizationId, connectionId = connectionId)
            }
        }
    }

    @Scheduled(fixedDelay = HEARTBEAT_INTERVAL)
    fun sendHeartbeats() {
        emitters.forEach { (organizationId, organizationEmitters) ->
            organizationEmitters.forEach { (connectionId, emitter) ->
                try {
                    emitter.send(SseEmitter.event().comment("heartbeat"))
                } catch (_: Exception) {
                    remove(organizationId = organizationId, connectionId = connectionId)
                }
            }
        }
    }

    private fun configureLifecycle(organizationId: UUID, connectionId: UUID, emitter: SseEmitter) {
        emitter.onCompletion { remove(organizationId = organizationId, connectionId = connectionId) }
        emitter.onTimeout { remove(organizationId = organizationId, connectionId = connectionId) }
        emitter.onError { remove(organizationId = organizationId, connectionId = connectionId) }
    }

    private fun remove(organizationId: UUID, connectionId: UUID) {
        emitters.computeIfPresent(organizationId) {
                _, organizationEmitters ->
            organizationEmitters.remove(connectionId)

            organizationEmitters.takeUnless {
                it.isEmpty()
            }
        }
    }

    companion object {
        private const val MIN_CONNECTION_TIMEOUT = 1000L
        private const val MAX_CONNECTION_TIMEOUT = 15 * 60 * 1000L
        private const val RECONNECT_TIME = 3_000L
        private const val HEARTBEAT_INTERVAL = 25_000L
    }
}