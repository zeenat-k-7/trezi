package com.trezi.ai.repository;

import com.trezi.ai.entity.ReasoningTrace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ReasoningTraceRepository extends JpaRepository<ReasoningTrace, UUID> {
    Optional<ReasoningTrace> findByAiExecutionId(UUID aiExecutionId);
}
