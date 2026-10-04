CREATE TABLE reasoning_traces (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    ai_execution_id UUID NOT NULL UNIQUE,
    intent VARCHAR(100),
    context_summary TEXT,
    evidence_summary TEXT,
    calculation_summary TEXT,
    decision TEXT,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_reasoning_traces_execution
        FOREIGN KEY (ai_execution_id) REFERENCES ai_executions(id) ON DELETE CASCADE
);
