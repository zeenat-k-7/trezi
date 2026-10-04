from models import ChatRequest, ChatResponse, Source, ReasoningTrace, ComplianceResult, AgentContribution
from rag import RAGRetriever
from agents.diagnostic import DiagnosticAgent
from agents.pedagogical import PedagogicalAgent
from agents.compliance import ComplianceAgent

class Orchestrator:
    def __init__(self, gemini_client, rag_retriever, settings):
        self.gemini_client = gemini_client
        self.diagnostic = DiagnosticAgent(gemini_client)
        self.pedagogical = PedagogicalAgent(gemini_client)
        self.compliance = ComplianceAgent(gemini_client)
        self.rag = rag_retriever
        self.settings = settings

    async def process_chat(self, request: ChatRequest, auth_header: str = None) -> ChatResponse:
        user_ctx = request.user_context.model_dump() if request.user_context else {}
        agents_used = []

        # Step 1.5: Fetch comprehensive user context from Spring Boot
        if auth_header:
            import httpx
            import asyncio
            headers = {"Authorization": auth_header}
            base_url = "http://localhost:8080/api"

            async with httpx.AsyncClient(timeout=5.0) as client:
                try:
                    profile_res, snap_res, assess_res = await asyncio.gather(
                        client.get(f"{base_url}/users/me/profile", headers=headers),
                        client.get(f"{base_url}/financial/snapshot/latest", headers=headers),
                        client.get(f"{base_url}/assessments/latest-result", headers=headers),
                        return_exceptions=True
                    )

                    if isinstance(profile_res, httpx.Response) and profile_res.status_code == 200:
                        p = profile_res.json()
                        user_ctx['age'] = p.get('age')
                        user_ctx['profession'] = p.get('profession')
                        user_ctx['risk_preference'] = p.get('riskPreference')
                        user_ctx['investment_experience'] = p.get('investmentExperience')
                        user_ctx['financial_knowledge_rating'] = p.get('financialKnowledgeRating')
                        user_ctx['preferred_explanation_style'] = p.get('preferredExplanationStyle')

                    if isinstance(snap_res, httpx.Response) and snap_res.status_code == 200:
                        s = snap_res.json()
                        user_ctx['monthly_income'] = s.get('monthlyIncome')
                        user_ctx['monthly_expenses'] = s.get('monthlyExpenses')
                        user_ctx['total_savings'] = s.get('totalSavings')
                        user_ctx['total_debt'] = s.get('totalDebt')
                        user_ctx['total_investments'] = s.get('totalInvestments')
                        user_ctx['net_worth'] = s.get('netWorth')

                    if isinstance(assess_res, httpx.Response) and assess_res.status_code == 200:
                        a = assess_res.json()
                        user_ctx['literacy_level'] = a.get('literacyLevel')
                        user_ctx['assessment_budgeting'] = a.get('budgetingScore')
                        user_ctx['assessment_saving'] = a.get('savingScore')
                        user_ctx['assessment_investing'] = a.get('investmentScore')
                        user_ctx['assessment_risk'] = a.get('riskScore')
                        user_ctx['assessment_digital'] = a.get('digitalFinanceScore')
                        user_ctx['assessment_tax'] = a.get('taxScore')

                except Exception as e:
                    print(f"Failed to fetch context from Spring Boot: {e}")


        # Step 1: RAG retrieval
        rag_results = []
        try:
            rag_results = self.rag.search(request.message, top_k=5)
        except Exception as e:
            print(f"RAG search failed: {e}")
            # Continue without RAG — degrade gracefully

        # Step 2: Diagnostic Agent
        diagnostic_output = ""
        try:
            diagnostic_output = self.diagnostic.analyze(user_ctx, request.message)
            agents_used.append(AgentContribution(agent_name="Diagnostic", contribution=diagnostic_output))
        except Exception as e:
            diagnostic_output = f"Diagnostic analysis unavailable: {e}"

        # Step 3: Pedagogical Agent (main response generation)
        pedagogical_output = ""
        try:
            pedagogical_output = self.pedagogical.explain(request.message, rag_results, user_ctx, diagnostic_output)
            agents_used.append(AgentContribution(agent_name="Pedagogical", contribution=pedagogical_output))
        except Exception as e:
            pedagogical_output = f"I apologize, but I'm unable to generate a response at this time. Error: {e}"

        # Step 4: Compliance Agent
        compliance_result = {'checks': [], 'overall_safe': True, 'modified_response': None}
        try:
            compliance_result = self.compliance.check(request.message, pedagogical_output, user_ctx)
            agents_used.append(AgentContribution(agent_name="Compliance", contribution="Compliance review completed"))
        except Exception as e:
            print(f"Compliance check failed: {e}")

        # Step 5: Apply compliance modifications if needed
        final_response = pedagogical_output
        if compliance_result.get('modified_response'):
            final_response = compliance_result['modified_response']

        # Step 6: Build sources
        sources = [
            Source(
                chunk_id=r['chunk_id'],
                document_title=r['document_title'],
                section_title=r.get('section_title'),
                source_name=r['source_name'],
                relevance_score=r['relevance_score'],
                content_snippet=r['content'][:200],
            )
            for r in rag_results
        ]

        # Step 7: Build reasoning trace
        prof_summary = []
        if user_ctx.get('literacy_level'): prof_summary.append(f"Literacy: {user_ctx['literacy_level']}")
        if user_ctx.get('risk_preference'): prof_summary.append(f"Risk: {user_ctx['risk_preference']}")
        if user_ctx.get('monthly_income') is not None: prof_summary.append("Financial data: Present")
        else: prof_summary.append("Financial data: Missing")

        reasoning_trace = ReasoningTrace(
            intent=self._classify_intent(request.message),
            context_summary=f"User profile: {', '.join(prof_summary)}. Diagnostic: {diagnostic_output[:200] if diagnostic_output else 'N/A'}",
            evidence_summary=f"Retrieved {len(rag_results)} relevant knowledge sources." + (f" Top source: {rag_results[0]['document_title']}" if rag_results else ""),
            calculation_summary=None,
            decision=f"Generated educational response using Pedagogical Agent. Compliance status: {'PASS' if compliance_result.get('overall_safe', True) else 'MODIFIED'}. Agents used: {', '.join(a.agent_name for a in agents_used)}.",
        )

        # Step 8: Build compliance checks
        compliance_checks = [
            ComplianceResult(
                check_type=c.get('check_type', 'GENERAL'),
                status=c.get('status', 'PASS'),
                risk_level=c.get('risk_level', 'LOW'),
                issues=c.get('issues'),
                required_action=c.get('required_action'),
            )
            for c in compliance_result.get('checks', [])
        ]

        return ChatResponse(
            response=final_response,
            sources=sources,
            reasoning_trace=reasoning_trace,
            compliance_checks=compliance_checks,
            agents_used=agents_used,
            model_provider="google",
            model_name=self.settings.GEMINI_MODEL,
        )

    def _classify_intent(self, message: str) -> str:
        """Simple rule-based intent classification."""
        msg = message.lower()
        if any(w in msg for w in ['budget', 'spending', 'expense']): return 'BUDGETING'
        if any(w in msg for w in ['save', 'saving', 'emergency fund']): return 'SAVING'
        if any(w in msg for w in ['invest', 'stock', 'mutual fund', 'sip', 'portfolio']): return 'INVESTING'
        if any(w in msg for w in ['tax', 'deduction', 'section 80']): return 'TAX_PLANNING'
        if any(w in msg for w in ['loan', 'debt', 'emi', 'credit']): return 'DEBT_MANAGEMENT'
        if any(w in msg for w in ['insurance', 'term plan', 'health insurance']): return 'INSURANCE'
        if any(w in msg for w in ['retire', 'retirement', 'pension']): return 'RETIREMENT'
        if any(w in msg for w in ['risk', 'volatility']): return 'RISK_ASSESSMENT'
        return 'GENERAL_FINANCE'
