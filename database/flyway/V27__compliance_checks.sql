CREATE TABLE compliance_checks (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    ai_execution_id UUID NOT NULL,
    check_type VARCHAR(100) NOT NULL,
    status VARCHAR(20) NOT NULL,
    risk_level VARCHAR(20) NOT NULL,
    issues TEXT,
    required_action TEXT,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_compliance_checks_execution
        FOREIGN KEY (ai_execution_id) REFERENCES ai_executions(id) ON DELETE CASCADE,

    CONSTRAINT ck_compliance_checks_status
        CHECK (status IN ('PASS','MODIFY','BLOCK','ESCALATE')),

    CONSTRAINT ck_compliance_checks_risk
        CHECK (risk_level IN ('LOW','MEDIUM','HIGH','CRITICAL'))
);
