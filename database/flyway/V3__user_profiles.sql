CREATE TABLE user_profiles (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL UNIQUE,
    age SMALLINT,
    profession VARCHAR(100),
    employment_type VARCHAR(30),
    dependents_count SMALLINT,
    primary_financial_goal VARCHAR(50),
    risk_preference VARCHAR(30),
    investment_experience VARCHAR(30),
    financial_knowledge_rating VARCHAR(30),
    preferred_language VARCHAR(30),
    preferred_explanation_style VARCHAR(30),
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_user_profiles_user
        FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,

    CONSTRAINT ck_user_profiles_age
        CHECK (age IS NULL OR age BETWEEN 0 AND 120),

    CONSTRAINT ck_user_profiles_dependents
        CHECK (dependents_count IS NULL OR dependents_count >= 0),

    CONSTRAINT ck_user_profiles_risk
        CHECK (risk_preference IS NULL OR risk_preference IN ('CONSERVATIVE','MODERATE','AGGRESSIVE')),

    CONSTRAINT ck_user_profiles_experience
        CHECK (investment_experience IS NULL OR investment_experience IN ('NONE','BEGINNER','INTERMEDIATE','EXPERIENCED'))
);
