package com.trezi.assessment.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public class AssessmentResultResponse {
    private UUID assessmentId;
    private BigDecimal overallScore;
    private BigDecimal budgetingScore;
    private BigDecimal savingScore;
    private BigDecimal investmentScore;
    private BigDecimal riskScore;
    private BigDecimal digitalFinanceScore;
    private BigDecimal taxScore;
    private String literacyLevel;
    private Instant completedAt;

    // Getters and Setters
    public UUID getAssessmentId() { return assessmentId; }
    public void setAssessmentId(UUID assessmentId) { this.assessmentId = assessmentId; }
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
    public String getLiteracyLevel() { return literacyLevel; }
    public void setLiteracyLevel(String literacyLevel) { this.literacyLevel = literacyLevel; }
    public Instant getCompletedAt() { return completedAt; }
    public void setCompletedAt(Instant completedAt) { this.completedAt = completedAt; }
}
