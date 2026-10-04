package com.trezi.financial.budget.repository;

import com.trezi.financial.budget.entity.Budget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BudgetRepository extends JpaRepository<Budget, UUID> {

    List<Budget> findByUser_IdOrderByPeriodStartDesc(UUID userId);

    Optional<Budget> findByIdAndUser_Id(UUID id, UUID userId);
}
