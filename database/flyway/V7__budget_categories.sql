CREATE TABLE budget_categories (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    budget_id UUID NOT NULL,
    category VARCHAR(100) NOT NULL,
    limit_amount NUMERIC(15,2) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_budget_categories_budget
        FOREIGN KEY (budget_id) REFERENCES budgets(id) ON DELETE CASCADE,

    CONSTRAINT uq_budget_categories_budget_category
        UNIQUE (budget_id, category),

    CONSTRAINT ck_budget_categories_limit
        CHECK (limit_amount >= 0)
);
