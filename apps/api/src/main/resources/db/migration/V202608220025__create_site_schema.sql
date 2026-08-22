CREATE SCHEMA site;

CREATE TABLE site.site
(
    id              UUID,
    organization_id UUID         NOT NULL,
    name            VARCHAR(150) NOT NULL,
    status          VARCHAR(30)  NOT NULL,
    created_at      TIMESTAMPTZ  NOT NULL,
    updated_at      TIMESTAMPTZ,

    CONSTRAINT pk_organization_id PRIMARY KEY (id),
    CONSTRAINT fk_site_organization FOREIGN KEY (organization_id) REFERENCES organization.organization (id)
);

CREATE INDEX ix_site_organization_id ON site.site (organization_id);