package com.trezi.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "user_profiles")
public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(name = "age")
    private Short age;

    @Column(name = "profession", length = 100)
    private String profession;

    @Column(name = "employment_type", length = 30)
    private String employmentType;

    @Column(name = "dependents_count")
    private Short dependentsCount;

    @Column(name = "primary_financial_goal", length = 50)
    private String primaryFinancialGoal;

    @Enumerated(EnumType.STRING)
    @Column(name = "risk_preference", length = 30)
    private RiskPreference riskPreference;

    @Enumerated(EnumType.STRING)
    @Column(name = "investment_experience", length = 30)
    private InvestmentExperience investmentExperience;

    @Column(name = "financial_knowledge_rating", length = 30)
    private String financialKnowledgeRating;

    @Column(name = "preferred_language", length = 30)
    private String preferredLanguage;

    @Column(name = "preferred_explanation_style", length = 30)
    private String preferredExplanationStyle;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    public UserProfile() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Short getAge() {
        return age;
    }

    public void setAge(Short age) {
        this.age = age;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getEmploymentType() {
        return employmentType;
    }

    public void setEmploymentType(String employmentType) {
        this.employmentType = employmentType;
    }

    public Short getDependentsCount() {
        return dependentsCount;
    }

    public void setDependentsCount(Short dependentsCount) {
        this.dependentsCount = dependentsCount;
    }

    public String getPrimaryFinancialGoal() {
        return primaryFinancialGoal;
    }

    public void setPrimaryFinancialGoal(String primaryFinancialGoal) {
        this.primaryFinancialGoal = primaryFinancialGoal;
    }

    public RiskPreference getRiskPreference() {
        return riskPreference;
    }

    public void setRiskPreference(RiskPreference riskPreference) {
        this.riskPreference = riskPreference;
    }

    public InvestmentExperience getInvestmentExperience() {
        return investmentExperience;
    }

    public void setInvestmentExperience(InvestmentExperience investmentExperience) {
        this.investmentExperience = investmentExperience;
    }

    public String getFinancialKnowledgeRating() {
        return financialKnowledgeRating;
    }

    public void setFinancialKnowledgeRating(String financialKnowledgeRating) {
        this.financialKnowledgeRating = financialKnowledgeRating;
    }

    public String getPreferredLanguage() {
        return preferredLanguage;
    }

    public void setPreferredLanguage(String preferredLanguage) {
        this.preferredLanguage = preferredLanguage;
    }

    public String getPreferredExplanationStyle() {
        return preferredExplanationStyle;
    }

    public void setPreferredExplanationStyle(String preferredExplanationStyle) {
        this.preferredExplanationStyle = preferredExplanationStyle;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}
