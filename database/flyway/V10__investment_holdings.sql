CREATE TABLE investment_holdings (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL,
    instrument_name VARCHAR(200) NOT NULL,
    instrument_type VARCHAR(50) NOT NULL,
    quantity NUMERIC(20,8) NOT NULL,
    invested_amount NUMERIC(15,2) NOT NULL,
    current_value NUMERIC(15,2) NOT NULL,
    purchase_date DATE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_investment_holdings_user
        FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,

    CONSTRAINT ck_investment_holdings_quantity
        CHECK (quantity > 0),

    CONSTRAINT ck_investment_holdings_amounts
        CHECK (invested_amount >= 0 AND current_value >= 0)
);
