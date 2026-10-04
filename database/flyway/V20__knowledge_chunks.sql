CREATE TABLE knowledge_chunks (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    document_version_id UUID NOT NULL,
    chunk_index INTEGER NOT NULL,
    section_title VARCHAR(500),
    content TEXT NOT NULL,
    embedding VECTOR(1536) NOT NULL,
    token_count INTEGER,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_knowledge_chunks_document_version
        FOREIGN KEY (document_version_id)
        REFERENCES knowledge_document_versions(id)
        ON DELETE CASCADE,

    CONSTRAINT uq_knowledge_chunks_position
        UNIQUE (document_version_id, chunk_index),

    CONSTRAINT ck_knowledge_chunks_index
        CHECK (chunk_index >= 0),

    CONSTRAINT ck_knowledge_chunks_token_count
        CHECK (token_count IS NULL OR token_count > 0)
);
