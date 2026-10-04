CREATE TABLE ai_executions (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL,
    conversation_id UUID,
    request_message_id UUID,
    intent VARCHAR(100),
    execution_status VARCHAR(30) NOT NULL DEFAULT 'RUNNING',
    model_provider VARCHAR(100),
    model_name VARCHAR(150),
    started_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    completed_at TIMESTAMPTZ,
    error_message TEXT,

    CONSTRAINT fk_ai_executions_user
        FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,

    CONSTRAINT fk_ai_executions_conversation
        FOREIGN KEY (conversation_id) REFERENCES conversations(id) ON DELETE CASCADE,

    CONSTRAINT fk_ai_executions_request_message
        FOREIGN KEY (request_message_id) REFERENCES messages(id) ON DELETE SET NULL,

    CONSTRAINT ck_ai_executions_status
        CHECK (execution_status IN ('RUNNING','COMPLETED','FAILED','BLOCKED')),

    CONSTRAINT ck_ai_executions_completion
        CHECK (completed_at IS NULL OR completed_at >= started_at)
);
