CREATE TABLE knowledge_documents (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    source_id UUID NOT NULL,
    title VARCHAR(500) NOT NULL,
    document_type VARCHAR(50) NOT NULL,
    source_url TEXT NOT NULL,
    document_identifier VARCHAR(255),
    publication_date DATE,
    effective_date DATE,
    retrieved_at TIMESTAMPTZ NOT NULL,
    content_hash VARCHAR(64) NOT NULL,
    status VARCHAR(30) NOT NULL DEFAULT 'ACTIVE',
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_knowledge_documents_source
        FOREIGN KEY (source_id) REFERENCES knowledge_sources(id) ON DELETE RESTRICT,

    CONSTRAINT ck_knowledge_documents_status
        CHECK (status IN ('ACTIVE','SUPERSEDED','ARCHIVED','FAILED'))
);
