CREATE TABLE transactions (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL,
    transaction_date DATE NOT NULL,
    transaction_type VARCHAR(20) NOT NULL,
    category VARCHAR(100) NOT NULL,
    amount NUMERIC(15,2) NOT NULL,
    description VARCHAR(500),
    source VARCHAR(30) NOT NULL DEFAULT 'USER_ENTERED',
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_transactions_user
        FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,

    CONSTRAINT ck_transactions_type
        CHECK (transaction_type IN ('INCOME','EXPENSE','TRANSFER')),

    CONSTRAINT ck_transactions_source
        CHECK (source IN ('USER_ENTERED','SYSTEM_IMPORTED','SYSTEM_GENERATED')),

    CONSTRAINT ck_transactions_amount
        CHECK (amount > 0)
);
