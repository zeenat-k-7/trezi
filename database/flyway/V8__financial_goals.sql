CREATE TABLE financial_goals (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL,
    name VARCHAR(100) NOT NULL,
    target_amount NUMERIC(15,2) NOT NULL,
    target_date DATE,
    current_amount NUMERIC(15,2) NOT NULL DEFAULT 0,
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_financial_goals_user
        FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,

    CONSTRAINT ck_financial_goals_target
        CHECK (target_amount > 0),

    CONSTRAINT ck_financial_goals_current
        CHECK (current_amount >= 0),

    CONSTRAINT ck_financial_goals_status
        CHECK (status IN ('ACTIVE','COMPLETED','PAUSED','ARCHIVED'))
);
