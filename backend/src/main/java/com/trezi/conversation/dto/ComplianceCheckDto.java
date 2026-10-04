package com.trezi.conversation.dto;

public class ComplianceCheckDto {
    private String checkType;
    private String status;
    private String riskLevel;
    private String issues;
    private String requiredAction;

    // Getters and Setters
    public String getCheckType() { return checkType; }
    public void setCheckType(String checkType) { this.checkType = checkType; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getRiskLevel() { return riskLevel; }
    public void setRiskLevel(String riskLevel) { this.riskLevel = riskLevel; }
    public String getIssues() { return issues; }
    public void setIssues(String issues) { this.issues = issues; }
    public String getRequiredAction() { return requiredAction; }
    public void setRequiredAction(String requiredAction) { this.requiredAction = requiredAction; }
}
