from pydantic import BaseModel, Field, ConfigDict


class UserContext(BaseModel):
    model_config = ConfigDict(
        populate_by_name=True
    )

    user_id: str | None = Field(default=None, alias="userId")
    age: int | None = None
    profession: str | None = None

    risk_preference: str | None = Field(
        default=None,
        alias="riskPreference"
    )

    investment_experience: str | None = Field(
        default=None,
        alias="investmentExperience"
    )

    financial_knowledge_rating: str | None = Field(
        default=None,
        alias="financialKnowledgeRating"
    )

    preferred_explanation_style: str | None = Field(
        default=None,
        alias="preferredExplanationStyle"
    )

    monthly_income: float | None = Field(
        default=None,
        alias="monthlyIncome"
    )

    monthly_expenses: float | None = Field(
        default=None,
        alias="monthlyExpenses"
    )

    total_savings: float | None = Field(
        default=None,
        alias="totalSavings"
    )

    total_debt: float | None = Field(
        default=None,
        alias="totalDebt"
    )

    total_investments: float | None = Field(
        default=None,
        alias="totalInvestments"
    )

    net_worth: float | None = Field(
        default=None,
        alias="netWorth"
    )

    literacy_level: str | None = Field(
        default=None,
        alias="literacyLevel"
    )

    assessment_budgeting: float | None = Field(
        default=None,
        alias="assessmentBudgeting"
    )

    assessment_saving: float | None = Field(
        default=None,
        alias="assessmentSaving"
    )

    assessment_investing: float | None = Field(
        default=None,
        alias="assessmentInvesting"
    )

    assessment_risk: float | None = Field(
        default=None,
        alias="assessmentRisk"
    )

    assessment_digital: float | None = Field(
        default=None,
        alias="assessmentDigital"
    )

    assessment_tax: float | None = Field(
        default=None,
        alias="assessmentTax"
    )


class ChatRequest(BaseModel):
    model_config = ConfigDict(
        populate_by_name=True
    )

    user_id: str
    message: str

    conversation_id: str | None = None

    conversation_history: list[dict] = Field(
        default_factory=list
    )

    user_context: UserContext | None = None


class Source(BaseModel):
    chunk_id: str
    document_title: str
    section_title: str | None = None
    source_name: str
    relevance_score: float
    content_snippet: str


class ReasoningTrace(BaseModel):
    intent: str
    context_summary: str
    evidence_summary: str
    calculation_summary: str | None = None
    decision: str


class ComplianceResult(BaseModel):
    check_type: str
    status: str
    risk_level: str
    issues: str | None = None
    required_action: str | None = None


class AgentContribution(BaseModel):
    agent_name: str
    contribution: str


class ChatResponse(BaseModel):
    response: str
    reasoning_trace: ReasoningTrace

    sources: list[Source] = Field(
        default_factory=list
    )

    compliance_checks: list[ComplianceResult] = Field(
        default_factory=list
    )

    agents_used: list[AgentContribution] = Field(
        default_factory=list
    )

    model_provider: str = "google"
    model_name: str = "gemini-2.5-flash"