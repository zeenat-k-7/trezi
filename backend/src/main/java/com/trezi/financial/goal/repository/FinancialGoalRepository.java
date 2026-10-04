package com.trezi.financial.goal.repository;

import com.trezi.financial.goal.entity.FinancialGoal;
import com.trezi.financial.goal.entity.GoalStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface FinancialGoalRepository extends JpaRepository<FinancialGoal, UUID> {

    List<FinancialGoal> findByUser_IdOrderByCreatedAtDesc(UUID userId);

    Optional<FinancialGoal> findByIdAndUser_Id(UUID id, UUID userId);

    List<FinancialGoal> findByUser_IdAndStatus(UUID userId, GoalStatus status);
}
