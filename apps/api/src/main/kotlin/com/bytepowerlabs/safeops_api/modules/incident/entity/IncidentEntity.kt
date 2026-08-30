package com.bytepowerlabs.safeops_api.modules.incident.entity

import com.bytepowerlabs.safeops_api.modules.identity.entity.UserAccountEntity
import com.bytepowerlabs.safeops_api.modules.incident.exception.InvalidIncidentStatusTransitionException
import com.bytepowerlabs.safeops_api.modules.organization.entity.OrganizationEntity
import com.bytepowerlabs.safeops_api.modules.site.entity.SiteEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.time.Instant
import java.util.UUID
import kotlin.uuid.Uuid
import kotlin.uuid.toJavaUuid

@Entity
@Table(schema = "incident", name = "incident")
class IncidentEntity(
    @Id
    @Column(name = "id", nullable = false, updatable = false)
    var id: UUID = Uuid.generateV7().toJavaUuid(),

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "organization_id", nullable = false, updatable = false)
    var organization: OrganizationEntity,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "site_id", nullable = false, updatable = false)
    var site: SiteEntity,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "reported_by", nullable = false, updatable = false)
    var reportedBy: UserAccountEntity,

    @Column(name = "title", nullable = false, length = 150)
    var title: String,

    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    var description: String,

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 30)
    var type: IncidentType,

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    var status: IncidentStatus = IncidentStatus.REPORTED,

    @Enumerated(EnumType.STRING)
    @Column(name = "severity", length = 30)
    var severity: IncidentSeverity? = null,

    @Column(name = "occurred_at", nullable = false)
    var occurredAt: Instant,

    @Column(name = "location", length = 150)
    var location: String? = null,

    @Column(name = "immediate_actions", columnDefinition = "TEXT")
    var immediateActions: String? = null,

    @Column(name = "closed_at")
    var closedAt: Instant? = null,

    @Column(name = "created_at", nullable = false, updatable = false)
    var createdAt: Instant = Instant.now(),

    @Column(name = "updated_at", nullable = false)
    var updatedAt: Instant = Instant.now(),
) {
    fun update(
        title: String?,
        description: String?,
        type: IncidentType?,
        severity: IncidentSeverity?,
        occurredAt: Instant?,
        location: String?,
        immediateActions: String?,
    ) {
        title?.let { this.title = title }
        description?.let { this.description = description }
        type?.let { this.type = type }
        severity?.let { this.severity = it }
        occurredAt?.let { this.occurredAt = it }
        location?.let { this.location = location }
        immediateActions?.let { this.immediateActions = it }
        updatedAt = Instant.now()
    }

    fun updateStatus(newStatus: IncidentStatus) {
        if (newStatus == status) {
            return
        }

        if (!status.canTransitionTo(newStatus)) {
            throw InvalidIncidentStatusTransitionException(
                currentStatus = status,
                newStatus = newStatus
            )
        }

        val now = Instant.now()

        status = newStatus
        closedAt =
            if (newStatus == IncidentStatus.CLOSED) now
            else null

        updatedAt = now
    }
}