CREATE TABLE ai_evidence (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    ai_execution_id UUID NOT NULL,
    knowledge_chunk_id UUID,
    financial_data_record_id UUID,
    evidence_type VARCHAR(50) NOT NULL,
    relevance_score NUMERIC(6,5),
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_ai_evidence_execution
        FOREIGN KEY (ai_execution_id) REFERENCES ai_executions(id) ON DELETE CASCADE,

    CONSTRAINT fk_ai_evidence_knowledge_chunk
        FOREIGN KEY (knowledge_chunk_id) REFERENCES knowledge_chunks(id) ON DELETE RESTRICT,

    CONSTRAINT fk_ai_evidence_financial_record
        FOREIGN KEY (financial_data_record_id) REFERENCES financial_data_records(id) ON DELETE RESTRICT,

    CONSTRAINT ck_ai_evidence_exactly_one_source
        CHECK (
            (knowledge_chunk_id IS NOT NULL)::INTEGER +
            (financial_data_record_id IS NOT NULL)::INTEGER = 1
        ),

    CONSTRAINT ck_ai_evidence_relevance
        CHECK (relevance_score IS NULL OR relevance_score BETWEEN 0 AND 1)
);
