CREATE TABLE goal_contributions (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    goal_id UUID NOT NULL,
    amount NUMERIC(15,2) NOT NULL,
    contribution_date DATE NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_goal_contributions_goal
        FOREIGN KEY (goal_id) REFERENCES financial_goals(id) ON DELETE CASCADE,

    CONSTRAINT ck_goal_contributions_amount
        CHECK (amount > 0)
);
