class DiagnosticAgent:
    def __init__(self, gemini_client):
        self.gemini_client = gemini_client

    def analyze(self, user_context: dict, message: str) -> str:
        system_prompt = """You are TREZI's Diagnostic Agent. Your role is to analyze the user's financial situation and literacy level to provide personalized context for financial education.

Based on the user's profile and financial data, provide a brief diagnostic assessment that includes:
1. Current financial health indicators
2. Knowledge gaps identified
3. Areas requiring attention
4. Recommended focus areas for education

Be factual, concise, and grounded in the provided data. Do not make assumptions beyond the data given. Do not provide specific financial advice — only diagnostic observations."""

        prompt = f"""User Profile:
{self._format_context(user_context)}

User's Question: {message}

Provide a diagnostic assessment relevant to the user's question."""

        return self.gemini_client.generate(prompt, system_instruction=system_prompt)

    def _format_context(self, ctx: dict) -> str:
        parts = []
        if ctx.get('age'): parts.append(f"Age: {ctx['age']}")
        if ctx.get('profession'): parts.append(f"Profession: {ctx['profession']}")
        if ctx.get('risk_preference'): parts.append(f"Risk Preference: {ctx['risk_preference']}")
        if ctx.get('investment_experience'): parts.append(f"Investment Experience: {ctx['investment_experience']}")

        parts.append("\\n--- Financial Snapshot ---")
        if ctx.get('monthly_income') is not None: parts.append(f"Monthly Income: ₹{ctx['monthly_income']:,.2f}")
        if ctx.get('monthly_expenses') is not None: parts.append(f"Monthly Expenses: ₹{ctx['monthly_expenses']:,.2f}")
        if ctx.get('total_savings') is not None: parts.append(f"Total Savings: ₹{ctx['total_savings']:,.2f}")
        if ctx.get('total_debt') is not None: parts.append(f"Total Debt: ₹{ctx['total_debt']:,.2f}")
        if ctx.get('total_investments') is not None: parts.append(f"Total Investments: ₹{ctx['total_investments']:,.2f}")
        if ctx.get('net_worth') is not None: parts.append(f"Net Worth: ₹{ctx['net_worth']:,.2f}")

        parts.append("\\n--- Assessment Result ---")
        if ctx.get('literacy_level'): parts.append(f"Overall Literacy Level: {ctx['literacy_level']}")
        if ctx.get('assessment_budgeting') is not None: parts.append(f"Budgeting Knowledge Score: {ctx['assessment_budgeting']}%")
        if ctx.get('assessment_saving') is not None: parts.append(f"Saving Knowledge Score: {ctx['assessment_saving']}%")
        if ctx.get('assessment_investing') is not None: parts.append(f"Investing Knowledge Score: {ctx['assessment_investing']}%")
        if ctx.get('assessment_risk') is not None: parts.append(f"Risk Knowledge Score: {ctx['assessment_risk']}%")
        if ctx.get('assessment_tax') is not None: parts.append(f"Tax Knowledge Score: {ctx['assessment_tax']}%")

        # Filter out sections that have no data under them
        formatted = '\\n'.join(parts).replace("\\n\\n---", "\\n---")
        return formatted if len(parts) > 2 else 'No profile data available.'
