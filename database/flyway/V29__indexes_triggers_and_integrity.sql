-- Case-insensitive account uniqueness.
CREATE UNIQUE INDEX ux_users_email_lower
    ON users (LOWER(email));

CREATE UNIQUE INDEX ux_users_phone
    ON users (phone_number)
    WHERE phone_number IS NOT NULL;

-- Ownership/query indexes.
CREATE INDEX ix_financial_snapshots_user_date
    ON financial_snapshots (user_id, snapshot_date DESC);

CREATE INDEX ix_transactions_user_date
    ON transactions (user_id, transaction_date DESC);

CREATE INDEX ix_budgets_user_period
    ON budgets (user_id, period_start DESC);

CREATE INDEX ix_budget_categories_budget
    ON budget_categories (budget_id);

CREATE INDEX ix_financial_goals_user_status
    ON financial_goals (user_id, status);

CREATE INDEX ix_goal_contributions_goal_date
    ON goal_contributions (goal_id, contribution_date DESC);

CREATE INDEX ix_investment_holdings_user
    ON investment_holdings (user_id);

CREATE INDEX ix_assessments_user_completed
    ON assessments (user_id, completed_at DESC);

CREATE INDEX ix_assessment_responses_assessment
    ON assessment_responses (assessment_id);

CREATE INDEX ix_messages_conversation_created
    ON messages (conversation_id, created_at ASC);

-- Knowledge/query indexes.
CREATE INDEX ix_knowledge_documents_source
    ON knowledge_documents (source_id);

CREATE INDEX ix_knowledge_document_versions_document
    ON knowledge_document_versions (document_id);

CREATE UNIQUE INDEX ux_knowledge_document_versions_current
    ON knowledge_document_versions (document_id)
    WHERE is_current = TRUE;

CREATE INDEX ix_knowledge_chunks_document_version
    ON knowledge_chunks (document_version_id);

CREATE INDEX ix_financial_data_records_source_date
    ON financial_data_records (source_id, observation_date DESC);

CREATE INDEX ix_financial_data_records_identifier_date
    ON financial_data_records (external_identifier, observation_date DESC)
    WHERE external_identifier IS NOT NULL;

-- AI governance indexes.
CREATE INDEX ix_ai_executions_user_started
    ON ai_executions (user_id, started_at DESC);

CREATE INDEX ix_ai_executions_conversation
    ON ai_executions (conversation_id, started_at DESC)
    WHERE conversation_id IS NOT NULL;

CREATE INDEX ix_ai_evidence_execution
    ON ai_evidence (ai_execution_id);

CREATE INDEX ix_ai_evidence_knowledge_chunk
    ON ai_evidence (knowledge_chunk_id)
    WHERE knowledge_chunk_id IS NOT NULL;

CREATE INDEX ix_ai_evidence_financial_record
    ON ai_evidence (financial_data_record_id)
    WHERE financial_data_record_id IS NOT NULL;

CREATE INDEX ix_compliance_checks_execution
    ON compliance_checks (ai_execution_id, created_at DESC);

-- Audit indexes.
CREATE INDEX ix_audit_logs_user_created
    ON audit_logs (user_id, created_at DESC)
    WHERE user_id IS NOT NULL;

CREATE INDEX ix_audit_logs_event_created
    ON audit_logs (event_type, created_at DESC);

CREATE INDEX ix_audit_logs_request_id
    ON audit_logs (request_id)
    WHERE request_id IS NOT NULL;

-- Vector search index.
CREATE INDEX ix_knowledge_chunks_embedding_hnsw
    ON knowledge_chunks
    USING hnsw (embedding vector_cosine_ops)
    WITH (m = 16, ef_construction = 64);

-- Automatic updated_at maintenance.
CREATE TRIGGER trg_users_updated_at
BEFORE UPDATE ON users
FOR EACH ROW EXECUTE FUNCTION set_updated_at();

CREATE TRIGGER trg_user_profiles_updated_at
BEFORE UPDATE ON user_profiles
FOR EACH ROW EXECUTE FUNCTION set_updated_at();

CREATE TRIGGER trg_transactions_updated_at
BEFORE UPDATE ON transactions
FOR EACH ROW EXECUTE FUNCTION set_updated_at();

CREATE TRIGGER trg_budgets_updated_at
BEFORE UPDATE ON budgets
FOR EACH ROW EXECUTE FUNCTION set_updated_at();

CREATE TRIGGER trg_budget_categories_updated_at
BEFORE UPDATE ON budget_categories
FOR EACH ROW EXECUTE FUNCTION set_updated_at();

CREATE TRIGGER trg_financial_goals_updated_at
BEFORE UPDATE ON financial_goals
FOR EACH ROW EXECUTE FUNCTION set_updated_at();

CREATE TRIGGER trg_investment_holdings_updated_at
BEFORE UPDATE ON investment_holdings
FOR EACH ROW EXECUTE FUNCTION set_updated_at();

CREATE TRIGGER trg_assessment_questions_updated_at
BEFORE UPDATE ON assessment_questions
FOR EACH ROW EXECUTE FUNCTION set_updated_at();

CREATE TRIGGER trg_assessment_results_updated_at
BEFORE UPDATE ON assessment_results
FOR EACH ROW EXECUTE FUNCTION set_updated_at();

CREATE TRIGGER trg_conversations_updated_at
BEFORE UPDATE ON conversations
FOR EACH ROW EXECUTE FUNCTION set_updated_at();

CREATE TRIGGER trg_knowledge_sources_updated_at
BEFORE UPDATE ON knowledge_sources
FOR EACH ROW EXECUTE FUNCTION set_updated_at();

CREATE TRIGGER trg_knowledge_documents_updated_at
BEFORE UPDATE ON knowledge_documents
FOR EACH ROW EXECUTE FUNCTION set_updated_at();

CREATE TRIGGER trg_financial_data_sources_updated_at
BEFORE UPDATE ON financial_data_sources
FOR EACH ROW EXECUTE FUNCTION set_updated_at();
