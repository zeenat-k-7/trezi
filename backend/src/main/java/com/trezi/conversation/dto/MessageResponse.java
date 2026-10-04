package com.trezi.conversation.dto;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class MessageResponse {
    private UUID id;
    private String role;
    private String content;
    private Instant createdAt;
    private List<SourceDto> sources;
    private ReasoningTraceDto reasoningTrace;
    private List<ComplianceCheckDto> complianceChecks;

    // Getters and Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public List<SourceDto> getSources() { return sources; }
    public void setSources(List<SourceDto> sources) { this.sources = sources; }
    public ReasoningTraceDto getReasoningTrace() { return reasoningTrace; }
    public void setReasoningTrace(ReasoningTraceDto reasoningTrace) { this.reasoningTrace = reasoningTrace; }
    public List<ComplianceCheckDto> getComplianceChecks() { return complianceChecks; }
    public void setComplianceChecks(List<ComplianceCheckDto> complianceChecks) { this.complianceChecks = complianceChecks; }
}
