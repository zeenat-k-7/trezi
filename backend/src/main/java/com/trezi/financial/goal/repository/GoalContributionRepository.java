package com.trezi.financial.goal.repository;

import com.trezi.financial.goal.entity.GoalContribution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface GoalContributionRepository extends JpaRepository<GoalContribution, UUID> {

    List<GoalContribution> findByGoalIdOrderByContributionDateDesc(UUID goalId);

    List<GoalContribution> findByGoal_User_IdAndGoal_IdOrderByContributionDateDesc(UUID userId, UUID goalId);

    Optional<GoalContribution> findByIdAndGoal_User_Id(UUID id, UUID userId);
}
