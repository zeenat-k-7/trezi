package com.trezi.conversation.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class AiChatResponse {
    @JsonProperty("response")
    private String response;

    @JsonProperty("sources")
    private List<SourceDto> sources;

    @JsonProperty("reasoning_trace")
    private ReasoningTraceDto reasoningTrace;

    @JsonProperty("compliance_checks")
    private List<ComplianceCheckDto> complianceChecks;

    @JsonProperty("agents_used")
    private List<AgentContributionDto> agentsUsed;

    @JsonProperty("model_provider")
    private String modelProvider;

    @JsonProperty("model_name")
    private String modelName;

    // Getters and Setters
    public String getResponse() { return response; }
    public void setResponse(String response) { this.response = response; }
    public List<SourceDto> getSources() { return sources; }
    public void setSources(List<SourceDto> sources) { this.sources = sources; }
    public ReasoningTraceDto getReasoningTrace() { return reasoningTrace; }
    public void setReasoningTrace(ReasoningTraceDto reasoningTrace) { this.reasoningTrace = reasoningTrace; }
    public List<ComplianceCheckDto> getComplianceChecks() { return complianceChecks; }
    public void setComplianceChecks(List<ComplianceCheckDto> complianceChecks) { this.complianceChecks = complianceChecks; }
    public List<AgentContributionDto> getAgentsUsed() { return agentsUsed; }
    public void setAgentsUsed(List<AgentContributionDto> agentsUsed) { this.agentsUsed = agentsUsed; }
    public String getModelProvider() { return modelProvider; }
    public void setModelProvider(String modelProvider) { this.modelProvider = modelProvider; }
    public String getModelName() { return modelName; }
    public void setModelName(String modelName) { this.modelName = modelName; }
}
