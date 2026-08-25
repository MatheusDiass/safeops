CREATE SCHEMA incident;

-- Required by the composite foreign key that enforces site-organization consistency
ALTER TABLE site.site
    ADD CONSTRAINT uk_site_id_organization_id UNIQUE (id, organization_id);

CREATE TABLE incident.incident
(
    id                UUID          NOT NULL,
    organization_id   UUID          NOT NULL,
    site_id           UUID          NOT NULL,
    reported_by       UUID          NOT NULL,
    title             VARCHAR(150)  NOT NULL,
    description       VARCHAR(3000) NOT NULL,
    type              VARCHAR(30)   NOT NULL,
    status            VARCHAR(30)   NOT NULL DEFAULT 'REPORTED',
    severity          VARCHAR(30),
    occurred_at       TIMESTAMPTZ   NOT NULL,
    location          VARCHAR(150),
    immediate_actions VARCHAR(5000),
    closed_at         TIMESTAMPTZ,
    created_at        TIMESTAMPTZ   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at        TIMESTAMPTZ   NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT pk_incident PRIMARY KEY (id),
    CONSTRAINT fk_incident_organization FOREIGN KEY (organization_id) REFERENCES organization.organization (id),
    CONSTRAINT fk_incident_reported_by FOREIGN KEY (reported_by) REFERENCES identity.user_account (id),
    CONSTRAINT fk_incident_site_organization FOREIGN KEY (site_id, organization_id) REFERENCES site.site (id, organization_id),

    CONSTRAINT ck_incident_type CHECK (
        type IN (
                 'ACCIDENT',
                 'NEAR_MISS',
                 'UNSAFE_CONDITION',
                 'ENVIRONMENTAL'
            )
        ),

    CONSTRAINT ck_incident_status CHECK (
        status IN (
                   'REPORTED',
                   'UNDER_REVIEW',
                   'ACTION_REQUIRED',
                   'CLOSED'
            )
        ),

    CONSTRAINT ck_incident_severity CHECK (
        severity IS NULL
            OR severity IN (
                            'LOW',
                            'MEDIUM',
                            'HIGH',
                            'CRITICAL'
            )
        ),

    CONSTRAINT ck_incident_closed_at
        CHECK (
            (status = 'CLOSED' AND closed_at IS NOT NULL)
                OR
            (status <> 'CLOSED' AND closed_at IS NULL)
            )
);

CREATE INDEX ix_incident_organization_site_created_at_id ON incident.incident (organization_id, site_id, created_at DESC, id DESC);