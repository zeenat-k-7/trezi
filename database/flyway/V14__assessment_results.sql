CREATE TABLE assessment_results (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    assessment_id UUID NOT NULL UNIQUE,
    overall_score NUMERIC(5,2) NOT NULL,
    budgeting_score NUMERIC(5,2) NOT NULL,
    saving_score NUMERIC(5,2) NOT NULL,
    investment_score NUMERIC(5,2) NOT NULL,
    risk_score NUMERIC(5,2) NOT NULL,
    digital_finance_score NUMERIC(5,2) NOT NULL,
    tax_score NUMERIC(5,2) NOT NULL,
    literacy_level VARCHAR(30) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_assessment_results_assessment
        FOREIGN KEY (assessment_id) REFERENCES assessments(id) ON DELETE CASCADE,

    CONSTRAINT ck_assessment_results_scores
        CHECK (
            overall_score BETWEEN 0 AND 100 AND
            budgeting_score BETWEEN 0 AND 100 AND
            saving_score BETWEEN 0 AND 100 AND
            investment_score BETWEEN 0 AND 100 AND
            risk_score BETWEEN 0 AND 100 AND
            digital_finance_score BETWEEN 0 AND 100 AND
            tax_score BETWEEN 0 AND 100
        ),

    CONSTRAINT ck_assessment_results_literacy
        CHECK (literacy_level IN ('BEGINNER','INTERMEDIATE','ADVANCED'))
);
