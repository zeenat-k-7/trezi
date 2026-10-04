-- Seed financial literacy assessment questions.
-- Each question stores its options as a JSON array in a separate column.
-- Since the schema only has question_text and correct_answer (VARCHAR), we use
-- the question_text to encode the full question and store the correct answer label.
-- The correct_answer stores one of the option texts (matched case-insensitively by AssessmentService).
-- Options are seeded via the options_json column added below — but since we cannot modify the schema,
-- we encode the full display text in question_text and use correct_answer for matching.
-- The backend QuestionDto will be updated to derive options from question metadata.

-- BUDGETING (3 questions)
INSERT INTO assessment_questions (question_text, topic, difficulty, correct_answer) VALUES
(
  'The 50/30/20 budgeting rule suggests allocating what percentage of income to savings and debt repayment?',
  'BUDGETING', 'EASY', '20%'
),
(
  'Which of the following is considered a "Need" in the 50/30/20 budgeting framework?',
  'BUDGETING', 'EASY', 'Rent and utilities'
),
(
  'If your monthly income is ₹60,000, how much should ideally go toward wants under the 50/30/20 rule?',
  'BUDGETING', 'MEDIUM', '₹18,000'
),

-- SAVING (3 questions)
(
  'An emergency fund should ideally cover how many months of essential living expenses?',
  'SAVING', 'EASY', '3 to 6 months'
),
(
  'Where should an emergency fund typically be kept?',
  'SAVING', 'EASY', 'A liquid savings account'
),
(
  'Which concept explains why ₹10,000 today is worth more than ₹10,000 one year from now?',
  'SAVING', 'MEDIUM', 'Time Value of Money'
),

-- INVESTING (3 questions)
(
  'What does SIP stand for in the context of mutual fund investing?',
  'INVESTING', 'EASY', 'Systematic Investment Plan'
),
(
  'Which type of mutual fund primarily invests in stocks and is suitable for long-term goals?',
  'INVESTING', 'EASY', 'Equity fund'
),
(
  'Rupee Cost Averaging is a benefit of which investment method?',
  'INVESTING', 'MEDIUM', 'SIP (Systematic Investment Plan)'
),

-- RISK (3 questions)
(
  'The risk-return tradeoff states that higher potential return is generally associated with:',
  'RISK', 'EASY', 'Higher risk'
),
(
  'A government bond is considered which type of investment?',
  'RISK', 'EASY', 'Low risk, low return'
),
(
  'An investor who cannot tolerate large losses should choose a portfolio that is:',
  'RISK', 'MEDIUM', 'Conservative'
),

-- DIGITAL_FINANCE (3 questions)
(
  'What does UPI stand for in Indian digital payments?',
  'DIGITAL_FINANCE', 'EASY', 'Unified Payments Interface'
),
(
  'Which of the following is the safest practice for online banking?',
  'DIGITAL_FINANCE', 'EASY', 'Never share your OTP with anyone'
),
(
  'CIBIL score is used by lenders to assess:',
  'DIGITAL_FINANCE', 'MEDIUM', 'Your creditworthiness'
),

-- TAX (3 questions)
(
  'Under Section 80C of the Income Tax Act, the maximum deduction allowed per year is:',
  'TAX', 'EASY', '₹1.5 lakh'
),
(
  'ELSS (Equity Linked Savings Scheme) has a mandatory lock-in period of:',
  'TAX', 'MEDIUM', '3 years'
),
(
  'Which of the following is NOT eligible for deduction under Section 80C?',
  'TAX', 'HARD', 'Short-term capital gains from stocks'
);
