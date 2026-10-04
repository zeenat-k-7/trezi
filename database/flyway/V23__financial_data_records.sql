CREATE TABLE financial_data_records (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    source_id UUID NOT NULL,
    data_type VARCHAR(100) NOT NULL,
    external_identifier VARCHAR(255),
    observation_date DATE NOT NULL,
    effective_from DATE,
    effective_to DATE,
    value NUMERIC(30,10) NOT NULL,
    unit VARCHAR(50) NOT NULL,
    metadata JSONB NOT NULL DEFAULT '{}'::JSONB,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_financial_data_records_source
        FOREIGN KEY (source_id) REFERENCES financial_data_sources(id) ON DELETE RESTRICT,

    CONSTRAINT ck_financial_data_records_effective_period
        CHECK (
            effective_to IS NULL OR
            effective_from IS NULL OR
            effective_to >= effective_from
        )
);
