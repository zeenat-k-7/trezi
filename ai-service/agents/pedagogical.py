class PedagogicalAgent:
    def __init__(self, gemini_client):
        self.gemini_client = gemini_client

    def explain(self, message: str, rag_context: list[dict], user_context: dict, diagnostic_output: str = "") -> str:
        system_prompt = """You are TREZI's Pedagogical Agent. Your role is to provide clear, accurate financial education grounded in verified source material.

Rules:
1. Base your explanations ONLY on the provided source material and established financial principles.
2. Adapt your explanation to the user's literacy level and preferred style.
3. Use examples and analogies appropriate for the user's background.
4. If the source material does not cover the topic, say so honestly.
5. Never fabricate financial facts, regulations, or statistics.
6. Always aim to educate — explain the 'why' behind concepts.
7. Use simple language for beginners, more technical language for advanced users.
8. Personalize the explanation using the user's diagnostic assessment and financial profile, but do not give definitive financial advice."""

        rag_text = self._format_sources(rag_context)

        # Build comprehensive context string
        profile_parts = []
        if user_context.get('literacy_level'): profile_parts.append(f"Literacy Level: {user_context['literacy_level']}")
        if user_context.get('monthly_income') is not None: profile_parts.append(f"Monthly Income: ₹{user_context['monthly_income']:,.2f}")
        if user_context.get('monthly_expenses') is not None: profile_parts.append(f"Monthly Expenses: ₹{user_context['monthly_expenses']:,.2f}")
        if user_context.get('total_savings') is not None: profile_parts.append(f"Total Savings: ₹{user_context['total_savings']:,.2f}")
        if user_context.get('total_debt') is not None: profile_parts.append(f"Total Debt: ₹{user_context['total_debt']:,.2f}")

        prompt = f"""User Context:
{', '.join(profile_parts) if profile_parts else 'Unknown'}

Diagnostic Assessment:
{diagnostic_output if diagnostic_output else 'N/A'}

Verified Source Material:
{rag_text}

User's Question: {message}

Provide a clear, personalized educational explanation grounded in the source material above and tailored to the diagnostic assessment."""

        return self.gemini_client.generate(prompt, system_instruction=system_prompt)

    def _format_sources(self, sources: list[dict]) -> str:
        if not sources:
            return 'No verified source material available for this topic.'
        parts = []
        for i, s in enumerate(sources, 1):
            parts.append(f"Source {i} [{s.get('source_name', 'Unknown')}] - {s.get('document_title', 'Untitled')}:")
            parts.append(s.get('content', ''))
            parts.append('')
        return '\\n'.join(parts)
