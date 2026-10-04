CREATE TABLE financial_data_sources (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(200) NOT NULL,
    organization VARCHAR(200) NOT NULL,
    data_type VARCHAR(100) NOT NULL,
    source_url TEXT NOT NULL,
    update_frequency VARCHAR(50),
    authority_level VARCHAR(20) NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT ck_financial_data_sources_authority
        CHECK (authority_level IN ('PRIMARY','SECONDARY','INTERNAL'))
);
