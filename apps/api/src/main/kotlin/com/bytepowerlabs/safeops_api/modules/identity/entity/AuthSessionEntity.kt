package com.bytepowerlabs.safeops_api.modules.identity.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.net.InetAddress
import java.util.UUID
import java.time.Instant
import kotlin.uuid.Uuid
import kotlin.uuid.toJavaUuid

@Entity
@Table(schema = "identity", name = "auth_session")
class AuthSessionEntity (
    @Id
    @Column(name = "id", nullable = false, updatable = false)
    var id: UUID = Uuid.generateV7().toJavaUuid(),

    @Column(name = "device_name", nullable = true, updatable = false)
    var deviceName: String? = null,

    @Column(name = "user_agent", nullable = true, updatable = false)
    var userAgent: String? = null,

    @Column(name = "ip_address", nullable = true, updatable = false)
    var ipAddress: InetAddress? = null,

    @Column(name = "expires_at", nullable = false, updatable = false)
    var expiresAt: Instant,

    @Column(name = "created_at", nullable = false, updatable = false)
    var createdAt: Instant = Instant.now(),

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_account_id", nullable = false, updatable = false)
    var userAccount: UserAccountEntity,
) {
    @Column(name = "revoked_at", nullable = true, updatable = true)
    var revokedAt: Instant? = null
        protected set

    fun revoke() {
        revokedAt = Instant.now()
    }
}