package com.trezi.ai.entity;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "reasoning_traces")
public class ReasoningTrace {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ai_execution_id", nullable = false, unique = true)
    private AiExecution aiExecution;

    @Column(name = "intent", length = 100)
    private String intent;

    @Column(name = "context_summary", columnDefinition = "TEXT")
    private String contextSummary;

    @Column(name = "evidence_summary", columnDefinition = "TEXT")
    private String evidenceSummary;

    @Column(name = "calculation_summary", columnDefinition = "TEXT")
    private String calculationSummary;

    @Column(name = "decision", columnDefinition = "TEXT")
    private String decision;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    public ReasoningTrace() {}

    public ReasoningTrace(AiExecution aiExecution) {
        this.aiExecution = aiExecution;
        this.createdAt = Instant.now();
    }

    public UUID getId() { return id; }
    public AiExecution getAiExecution() { return aiExecution; }
    public void setAiExecution(AiExecution aiExecution) { this.aiExecution = aiExecution; }
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
    public Instant getCreatedAt() { return createdAt; }
}
