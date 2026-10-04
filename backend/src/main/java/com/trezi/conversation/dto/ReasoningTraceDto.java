package com.trezi.conversation.dto;

public class ReasoningTraceDto {
    private String intent;
    private String contextSummary;
    private String evidenceSummary;
    private String calculationSummary;
    private String decision;

    // Getters and Setters
    public String getIntent() { return intent; }
    public void setIntent(String intent) { this.intent = intent; }
    public String getContextSummary() { return contextSummary; }
    public void setContextSummary(String contextSummary) { this.contextSummary = contextSummary; }
    public String getEvidenceSummary() { return evidenceSummary; }
    public void setEvidenceSummary(String evidenceSummary) { this.evidenceSummary = evidenceSummary; }
    public String getCalculationSummary() { return calculationSummary; }
    public void setCalculationSummary(String calculationSummary) { this.calculationSummary = calculationSummary; }
    public String getDecision() { return decision; }
    public void setDecision(String decision) { this.decision = decision; }
}
