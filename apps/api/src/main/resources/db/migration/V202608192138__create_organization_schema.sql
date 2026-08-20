CREATE SCHEMA organization;

CREATE TABLE organization.organization
(
    id         UUID,
    name       VARCHAR(150) NOT NULL,
    status     VARCHAR(30)  NOT NULL,
    created_at TIMESTAMPTZ  NOT NULL,
    updated_at TIMESTAMPTZ,

    CONSTRAINT pk_organization_id PRIMARY KEY (id)
);

CREATE TABLE organization.organization_membership
(
    id              UUID,
    organization_id UUID        NOT NULL,
    user_account_id UUID        NOT NULL,
    status          VARCHAR(30) NOt NULL,
    role            VARCHAR(30) NOT NULL,
    created_at      TIMESTAMPTZ NOT NULL,
    updated_at      TIMESTAMPTZ,

    CONSTRAINT pk_organization_membership_id PRIMARY KEY (id),
    CONSTRAINT fk_organization_membership_organization FOREIGN KEY (organization_id) REFERENCES organization.organization (id),
    CONSTRAINT fk_organization_membership_user FOREIGN KEY (user_account_id) REFERENCES identity.user_account (id),
    CONSTRAINT uk_organization_membership_user_organization UNIQUE (organization_id, user_account_id)
);

CREATE INDEX ix_organization_membership_user_id ON organization.organization_membership (user_account_id);