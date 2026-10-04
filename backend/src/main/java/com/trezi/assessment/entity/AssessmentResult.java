package com.trezi.assessment.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "assessment_results")
public class AssessmentResult {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assessment_id", nullable = false, unique = true)
    private Assessment assessment;

    @Column(name = "overall_score", nullable = false, precision = 5, scale = 2)
    private BigDecimal overallScore;

    @Column(name = "budgeting_score", nullable = false, precision = 5, scale = 2)
    private BigDecimal budgetingScore;

    @Column(name = "saving_score", nullable = false, precision = 5, scale = 2)
    private BigDecimal savingScore;

    @Column(name = "investment_score", nullable = false, precision = 5, scale = 2)
    private BigDecimal investmentScore;

    @Column(name = "risk_score", nullable = false, precision = 5, scale = 2)
    private BigDecimal riskScore;

    @Column(name = "digital_finance_score", nullable = false, precision = 5, scale = 2)
    private BigDecimal digitalFinanceScore;

    @Column(name = "tax_score", nullable = false, precision = 5, scale = 2)
    private BigDecimal taxScore;

    @Enumerated(EnumType.STRING)
    @Column(name = "literacy_level", nullable = false, length = 30)
    private LiteracyLevel literacyLevel;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt = Instant.now();

    public AssessmentResult() {}

    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = Instant.now();
    }

    public UUID getId() { return id; }
    public Assessment getAssessment() { return assessment; }
    public void setAssessment(Assessment assessment) { this.assessment = assessment; }
    public BigDecimal getOverallScore() { return overallScore; }
    public void setOverallScore(BigDecimal overallScore) { this.overallScore = overallScore; }
    public BigDecimal getBudgetingScore() { return budgetingScore; }
    public void setBudgetingScore(BigDecimal budgetingScore) { this.budgetingScore = budgetingScore; }
    public BigDecimal getSavingScore() { return savingScore; }
    public void setSavingScore(BigDecimal savingScore) { this.savingScore = savingScore; }
    public BigDecimal getInvestmentScore() { return investmentScore; }
    public void setInvestmentScore(BigDecimal investmentScore) { this.investmentScore = investmentScore; }
    public BigDecimal getRiskScore() { return riskScore; }
    public void setRiskScore(BigDecimal riskScore) { this.riskScore = riskScore; }
    public BigDecimal getDigitalFinanceScore() { return digitalFinanceScore; }
    public void setDigitalFinanceScore(BigDecimal digitalFinanceScore) { this.digitalFinanceScore = digitalFinanceScore; }
    public BigDecimal getTaxScore() { return taxScore; }
    public void setTaxScore(BigDecimal taxScore) { this.taxScore = taxScore; }
    public LiteracyLevel getLiteracyLevel() { return literacyLevel; }
    public void setLiteracyLevel(LiteracyLevel literacyLevel) { this.literacyLevel = literacyLevel; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
}
