package com.trezi.ai.entity;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "compliance_checks")
public class ComplianceCheck {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ai_execution_id", nullable = false)
    private AiExecution aiExecution;

    @Column(name = "check_type", nullable = false, length = 100)
    private String checkType;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private ComplianceStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "risk_level", nullable = false, length = 20)
    private RiskLevel riskLevel;

    @Column(name = "issues", columnDefinition = "TEXT")
    private String issues;

    @Column(name = "required_action", columnDefinition = "TEXT")
    private String requiredAction;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    public ComplianceCheck() {}

    public ComplianceCheck(AiExecution aiExecution, String checkType, ComplianceStatus status, RiskLevel riskLevel) {
        this.aiExecution = aiExecution;
        this.checkType = checkType;
        this.status = status;
        this.riskLevel = riskLevel;
        this.createdAt = Instant.now();
    }

    public UUID getId() { return id; }
    public AiExecution getAiExecution() { return aiExecution; }
    public void setAiExecution(AiExecution aiExecution) { this.aiExecution = aiExecution; }
    public String getCheckType() { return checkType; }
    public void setCheckType(String checkType) { this.checkType = checkType; }
    public ComplianceStatus getStatus() { return status; }
    public void setStatus(ComplianceStatus status) { this.status = status; }
    public RiskLevel getRiskLevel() { return riskLevel; }
    public void setRiskLevel(RiskLevel riskLevel) { this.riskLevel = riskLevel; }
    public String getIssues() { return issues; }
    public void setIssues(String issues) { this.issues = issues; }
    public String getRequiredAction() { return requiredAction; }
    public void setRequiredAction(String requiredAction) { this.requiredAction = requiredAction; }
    public Instant getCreatedAt() { return createdAt; }
}
