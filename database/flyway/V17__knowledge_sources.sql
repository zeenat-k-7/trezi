CREATE TABLE knowledge_sources (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(200) NOT NULL,
    organization VARCHAR(200) NOT NULL,
    source_type VARCHAR(50) NOT NULL,
    base_url TEXT NOT NULL,
    authority_level VARCHAR(20) NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT ck_knowledge_sources_authority
        CHECK (authority_level IN ('PRIMARY','SECONDARY','INTERNAL'))
);
