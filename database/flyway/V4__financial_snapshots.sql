CREATE TABLE financial_snapshots (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL,
    snapshot_date DATE NOT NULL,
    monthly_income NUMERIC(15,2) NOT NULL DEFAULT 0,
    monthly_expenses NUMERIC(15,2) NOT NULL DEFAULT 0,
    total_savings NUMERIC(15,2) NOT NULL DEFAULT 0,
    total_debt NUMERIC(15,2) NOT NULL DEFAULT 0,
    total_investments NUMERIC(15,2) NOT NULL DEFAULT 0,
    net_worth NUMERIC(15,2) NOT NULL DEFAULT 0,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_financial_snapshots_user
        FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,

    CONSTRAINT uq_financial_snapshots_user_date
        UNIQUE (user_id, snapshot_date),

    CONSTRAINT ck_financial_snapshots_nonnegative
        CHECK (
            monthly_income >= 0 AND
            monthly_expenses >= 0 AND
            total_savings >= 0 AND
            total_debt >= 0 AND
            total_investments >= 0
        )
);
