CREATE TABLE knowledge_ingestion_runs (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    source_id UUID NOT NULL,
    started_at TIMESTAMPTZ NOT NULL,
    completed_at TIMESTAMPTZ,
    status VARCHAR(30) NOT NULL,
    documents_found INTEGER NOT NULL DEFAULT 0,
    documents_processed INTEGER NOT NULL DEFAULT 0,
    documents_failed INTEGER NOT NULL DEFAULT 0,
    error_message TEXT,

    CONSTRAINT fk_knowledge_ingestion_runs_source
        FOREIGN KEY (source_id) REFERENCES knowledge_sources(id) ON DELETE RESTRICT,

    CONSTRAINT ck_knowledge_ingestion_runs_status
        CHECK (status IN ('RUNNING','COMPLETED','PARTIAL','FAILED')),

    CONSTRAINT ck_knowledge_ingestion_runs_counts
        CHECK (
            documents_found >= 0 AND
            documents_processed >= 0 AND
            documents_failed >= 0
        ),

    CONSTRAINT ck_knowledge_ingestion_runs_completion
        CHECK (completed_at IS NULL OR completed_at >= started_at)
);
