CREATE TABLE assessment_responses (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    assessment_id UUID NOT NULL,
    question_id UUID NOT NULL,
    selected_answer VARCHAR(255) NOT NULL,
    is_correct BOOLEAN NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_assessment_responses_assessment
        FOREIGN KEY (assessment_id) REFERENCES assessments(id) ON DELETE CASCADE,

    CONSTRAINT fk_assessment_responses_question
        FOREIGN KEY (question_id) REFERENCES assessment_questions(id) ON DELETE RESTRICT,

    CONSTRAINT uq_assessment_responses_question
        UNIQUE (assessment_id, question_id)
);
