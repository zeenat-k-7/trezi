package com.trezi.ai.repository;

import com.trezi.ai.entity.AiEvidence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AiEvidenceRepository extends JpaRepository<AiEvidence, UUID> {
    List<AiEvidence> findByAiExecutionId(UUID aiExecutionId);
}
