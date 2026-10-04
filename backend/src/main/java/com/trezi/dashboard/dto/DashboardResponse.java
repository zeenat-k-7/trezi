package com.trezi.dashboard.dto;

import com.trezi.conversation.dto.ConversationResponse;

import java.math.BigDecimal;
import java.util.List;

public class DashboardResponse {
    private String email;
    private boolean hasProfile;
    private boolean hasFinancialData;
    private boolean hasAssessment;
    private String literacyLevel;
    private BigDecimal overallScore;
    private BigDecimal monthlyIncome;
    private BigDecimal monthlyExpenses;
    private BigDecimal totalSavings;
    private BigDecimal netWorth;
    private int conversationCount;
    private List<ConversationResponse> recentConversations;

    // Getters and Setters
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public boolean isHasProfile() { return hasProfile; }
    public void setHasProfile(boolean hasProfile) { this.hasProfile = hasProfile; }
    public boolean isHasFinancialData() { return hasFinancialData; }
    public void setHasFinancialData(boolean hasFinancialData) { this.hasFinancialData = hasFinancialData; }
    public boolean isHasAssessment() { return hasAssessment; }
    public void setHasAssessment(boolean hasAssessment) { this.hasAssessment = hasAssessment; }
    public String getLiteracyLevel() { return literacyLevel; }
    public void setLiteracyLevel(String literacyLevel) { this.literacyLevel = literacyLevel; }
    public BigDecimal getOverallScore() { return overallScore; }
    public void setOverallScore(BigDecimal overallScore) { this.overallScore = overallScore; }
    public BigDecimal getMonthlyIncome() { return monthlyIncome; }
    public void setMonthlyIncome(BigDecimal monthlyIncome) { this.monthlyIncome = monthlyIncome; }
    public BigDecimal getMonthlyExpenses() { return monthlyExpenses; }
    public void setMonthlyExpenses(BigDecimal monthlyExpenses) { this.monthlyExpenses = monthlyExpenses; }
    public BigDecimal getTotalSavings() { return totalSavings; }
    public void setTotalSavings(BigDecimal totalSavings) { this.totalSavings = totalSavings; }
    public BigDecimal getNetWorth() { return netWorth; }
    public void setNetWorth(BigDecimal netWorth) { this.netWorth = netWorth; }
    public int getConversationCount() { return conversationCount; }
    public void setConversationCount(int conversationCount) { this.conversationCount = conversationCount; }
    public List<ConversationResponse> getRecentConversations() { return recentConversations; }
    public void setRecentConversations(List<ConversationResponse> recentConversations) { this.recentConversations = recentConversations; }
}
