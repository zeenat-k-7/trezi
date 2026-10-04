package com.trezi.assessment.repository;

import com.trezi.assessment.entity.AssessmentResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AssessmentResultRepository extends JpaRepository<AssessmentResult, UUID> {
    Optional<AssessmentResult> findByAssessmentId(UUID assessmentId);
    Optional<AssessmentResult> findFirstByAssessment_User_IdOrderByCreatedAtDesc(UUID userId);
}
