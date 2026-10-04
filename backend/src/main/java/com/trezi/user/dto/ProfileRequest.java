package com.trezi.user.dto;

public class ProfileRequest {
    private Short age;
    private String profession;
    private String employmentType;
    private Short dependentsCount;
    private String primaryFinancialGoal;
    private String riskPreference;
    private String investmentExperience;
    private String financialKnowledgeRating;
    private String preferredLanguage;
    private String preferredExplanationStyle;

    // getters and setters
    public Short getAge() { return age; }
    public void setAge(Short age) { this.age = age; }
    public String getProfession() { return profession; }
    public void setProfession(String profession) { this.profession = profession; }
    public String getEmploymentType() { return employmentType; }
    public void setEmploymentType(String employmentType) { this.employmentType = employmentType; }
    public Short getDependentsCount() { return dependentsCount; }
    public void setDependentsCount(Short dependentsCount) { this.dependentsCount = dependentsCount; }
    public String getPrimaryFinancialGoal() { return primaryFinancialGoal; }
    public void setPrimaryFinancialGoal(String primaryFinancialGoal) { this.primaryFinancialGoal = primaryFinancialGoal; }
    public String getRiskPreference() { return riskPreference; }
    public void setRiskPreference(String riskPreference) { this.riskPreference = riskPreference; }
    public String getInvestmentExperience() { return investmentExperience; }
    public void setInvestmentExperience(String investmentExperience) { this.investmentExperience = investmentExperience; }
    public String getFinancialKnowledgeRating() { return financialKnowledgeRating; }
    public void setFinancialKnowledgeRating(String financialKnowledgeRating) { this.financialKnowledgeRating = financialKnowledgeRating; }
    public String getPreferredLanguage() { return preferredLanguage; }
    public void setPreferredLanguage(String preferredLanguage) { this.preferredLanguage = preferredLanguage; }
    public String getPreferredExplanationStyle() { return preferredExplanationStyle; }
    public void setPreferredExplanationStyle(String preferredExplanationStyle) { this.preferredExplanationStyle = preferredExplanationStyle; }
}
