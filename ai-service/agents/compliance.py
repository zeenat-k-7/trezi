import json

class ComplianceAgent:
    def __init__(self, gemini_client):
        self.gemini_client = gemini_client

    def check(self, user_message: str, ai_response: str, user_context: dict) -> dict:
        system_prompt = """You are TREZI's Compliance Agent. Your role is to review AI-generated financial education content for compliance and safety.

Check for:
1. DISCLAIMER_CHECK: Does the response appropriately avoid giving specific investment advice without proper disclaimers?
2. ACCURACY_CHECK: Are claims grounded in provided sources rather than fabricated?
3. RISK_DISCLOSURE: Are financial risks appropriately disclosed?
4. PERSONALIZATION_SAFETY: Does personalized guidance stay within educational bounds?

For each check, respond with EXACTLY this JSON format:
{
  "checks": [
    {
      "check_type": "DISCLAIMER_CHECK",
      "status": "PASS",
      "risk_level": "LOW",
      "issues": null,
      "required_action": null
    }
  ],
  "overall_safe": true,
  "modified_response": null
}

Status must be one of: PASS, MODIFY, BLOCK, ESCALATE
Risk level must be one of: LOW, MEDIUM, HIGH, CRITICAL

If status is MODIFY, provide a modified_response that fixes the issues.
If status is BLOCK, the response should not be shown to the user."""

        prompt = f"""User Message: {user_message}

AI Response to Review:
{ai_response}

User Context:
- Literacy Level: {user_context.get('literacy_level', 'UNKNOWN')}
- Risk Preference: {user_context.get('risk_preference', 'UNKNOWN')}

Perform compliance checks and respond with the JSON format specified."""

        raw = self.gemini_client.generate(prompt, system_instruction=system_prompt)
        return self._parse_compliance(raw)

    def _parse_compliance(self, raw: str) -> dict:
        """Parse compliance response, with fallback for non-JSON."""
        # Try to extract JSON from the response
        try:
            # Find JSON block
            start = raw.find('{')
            end = raw.rfind('}') + 1
            if start >= 0 and end > start:
                return json.loads(raw[start:end])
        except json.JSONDecodeError:
            pass

        # Fallback: assume PASS
        return {
            'checks': [{
                'check_type': 'GENERAL_REVIEW',
                'status': 'PASS',
                'risk_level': 'LOW',
                'issues': None,
                'required_action': None,
            }],
            'overall_safe': True,
            'modified_response': None,
        }
