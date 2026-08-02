CREATE SCHEMA identity;

CREATE TABLE identity.user_account
(
    id                  UUID NOT NULL,
    name                VARCHAR(100) NOT NULL,
    email               VARCHAR(100) NOT NULL,
    password_hash       VARCHAR(255) NOT NULL,
    status              VARCHAR(30) NOT NULL,
    password_changed_at TIMESTAMPTZ,
    created_at          TIMESTAMPTZ NOT NULL,
    updated_at          TIMESTAMPTZ,

    CONSTRAINT pk_user_id PRIMARY KEY (id),
    CONSTRAINT uk_user_email UNIQUE (email),
    CONSTRAINT ck_user_account_status
        CHECK (status IN ('ACTIVE', 'BLOCKED', 'DISABLED'))
);

CREATE TABLE identity.auth_session (
    id UUID NOT NULL,
    user_account_id UUID NOT NULL,
    refresh_token_hash TEXT NOT NULL,
    device_name VARCHAR (100),
    user_agent VARCHAR (100),
    ip_address VARCHAR (20),
    expires_at TIMESTAMPTZ NOT NULL,
    last_used_at TIMESTAMPTZ,
    revoke_at TIMESTAMPTZ,
    created_at TIMESTAMPTZ NOT NULL,

    CONSTRAINT pk_auth_session_id PRIMARY KEY (id),
    CONSTRAINT fk_auth_session_user FOREIGN KEY (user_account_id) REFERENCES identity.user_account (id),
    CONSTRAINT uk_auth_session_refresh_token_hash UNIQUE (refresh_token_hash)
);

CREATE TABLE identity.password_reset_token
(
    id         UUID NOT NULL,
    user_account_id    UUID NOT NULL,
    token_hash TEXT NOT NULL,
    expires_at TIMESTAMPTZ NOT NULL,
    used_at    TIMESTAMPTZ,
    created_at TIMESTAMPTZ NOT NULL,

    CONSTRAINT pk_password_reset_token_id PRIMARY KEY (id),
    CONSTRAINT fk_password_reset_token_user FOREIGN KEY (user_account_id) REFERENCES identity.user_account (id),
    CONSTRAINT uk_password_reset_token_hash UNIQUE (token_hash)
);