package com.trezi.ai.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "ai_evidence")
public class AiEvidence {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ai_execution_id", nullable = false)
    private AiExecution aiExecution;

    @Column(name = "knowledge_chunk_id")
    private UUID knowledgeChunkId;

    @Column(name = "financial_data_record_id")
    private UUID financialDataRecordId;

    @Column(name = "evidence_type", nullable = false, length = 50)
    private String evidenceType;

    @Column(name = "relevance_score", precision = 6, scale = 5)
    private BigDecimal relevanceScore;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    public AiEvidence() {}

    public AiEvidence(AiExecution aiExecution, String evidenceType) {
        this.aiExecution = aiExecution;
        this.evidenceType = evidenceType;
        this.createdAt = Instant.now();
    }

    public UUID getId() { return id; }
    public AiExecution getAiExecution() { return aiExecution; }
    public void setAiExecution(AiExecution aiExecution) { this.aiExecution = aiExecution; }
    public UUID getKnowledgeChunkId() { return knowledgeChunkId; }
    public void setKnowledgeChunkId(UUID knowledgeChunkId) { this.knowledgeChunkId = knowledgeChunkId; }
    public UUID getFinancialDataRecordId() { return financialDataRecordId; }
    public void setFinancialDataRecordId(UUID financialDataRecordId) { this.financialDataRecordId = financialDataRecordId; }
    public String getEvidenceType() { return evidenceType; }
    public void setEvidenceType(String evidenceType) { this.evidenceType = evidenceType; }
    public BigDecimal getRelevanceScore() { return relevanceScore; }
    public void setRelevanceScore(BigDecimal relevanceScore) { this.relevanceScore = relevanceScore; }
    public Instant getCreatedAt() { return createdAt; }
}
