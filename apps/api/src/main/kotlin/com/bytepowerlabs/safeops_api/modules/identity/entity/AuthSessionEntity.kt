package com.bytepowerlabs.safeops_api.modules.identity.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.util.UUID
import java.time.Instant
import kotlin.uuid.Uuid
import kotlin.uuid.toJavaUuid

@Entity
@Table(schema = "identity", name = "auth_session")
class AuthSessionEntity (
    @Id
    @Column(name = "id", nullable = false, updatable = false)
    val id: UUID = Uuid.generateV7().toJavaUuid(),

    @Column(name = "user_account_id", nullable = false, updatable = false)
    val userAccountId: UUID,

    @Column(name = "refresh_token_hash", nullable = false, updatable = false)
    val refreshTokenHash: String,

    @Column(name = "device_name", nullable = true, updatable = false)
    val deviceName: String? = null,

    @Column(name = "user_agent", nullable = true, updatable = false)
    val userAgent: String? = null,

    @Column(name = "ip_address", nullable = true, updatable = false)
    val ipAddress: String? = null,

    @Column(name = "expires_at", nullable = false, updatable = false)
    val expiresAt: Instant,

    @Column(name = "last_used_at", nullable = true, updatable = false)
    val lastUsedAt: Instant? = null,

    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: Instant,
) {
    @Column(name = "revoke_at", nullable = true, updatable = false)
    var revokeAt: Instant? = null
        protected set

    fun revoke() {
        revokeAt = Instant.now()
    }
}