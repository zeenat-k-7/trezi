CREATE TABLE knowledge_document_versions (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    document_id UUID NOT NULL,
    version_label VARCHAR(100) NOT NULL,
    publication_date DATE,
    effective_date DATE,
    source_url TEXT NOT NULL,
    content_hash VARCHAR(64) NOT NULL,
    retrieved_at TIMESTAMPTZ NOT NULL,
    is_current BOOLEAN NOT NULL DEFAULT FALSE,
    raw_content TEXT NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_knowledge_document_versions_document
        FOREIGN KEY (document_id) REFERENCES knowledge_documents(id) ON DELETE CASCADE,

    CONSTRAINT uq_knowledge_document_versions_hash
        UNIQUE (document_id, content_hash)
);
