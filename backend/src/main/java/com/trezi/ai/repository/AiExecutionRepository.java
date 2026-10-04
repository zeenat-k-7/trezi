package com.trezi.ai.repository;

import com.trezi.ai.entity.AiExecution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AiExecutionRepository extends JpaRepository<AiExecution, UUID> {
}
