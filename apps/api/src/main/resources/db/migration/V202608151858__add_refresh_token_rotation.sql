ALTER TABLE identity.auth_session DROP COLUMN refresh_token_hash;
ALTER TABLE identity.auth_session DROP COLUMN last_used_at;
ALTER TABLE identity.auth_session RENAME COLUMN revoke_at TO revoked_at;
ALTER TABLE identity.auth_session ALTER COLUMN ip_address TYPE INET USING ip_address::INET;

CREATE TABLE identity.refresh_token
(
    id              UUID        NOT NULL,
    auth_session_id UUID        NOT NULL,
    token_hash      TEXT        NOT NULL,
    used_at         TIMESTAMPTZ,
    expires_at      TIMESTAMPTZ NOT NULL,
    created_at      TIMESTAMPTZ NOT NULL,

    CONSTRAINT pk_refresh_token_id PRIMARY KEY (id),
    CONSTRAINT fk_refresh_token_auth_session FOREIGN KEY (auth_session_id) REFERENCES identity.auth_session (id),
    CONSTRAINT uk_refresh_token_token_hash UNIQUE (token_hash)
);

CREATE INDEX ix_refresh_token_auth_session_id
    ON identity.refresh_token (auth_session_id);

CREATE UNIQUE INDEX uk_refresh_token_active_session
    ON identity.refresh_token (auth_session_id)
    WHERE used_at IS NULL;