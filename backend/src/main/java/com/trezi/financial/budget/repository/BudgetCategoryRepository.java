package com.trezi.financial.budget.repository;

import com.trezi.financial.budget.entity.BudgetCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BudgetCategoryRepository extends JpaRepository<BudgetCategory, UUID> {

    List<BudgetCategory> findByBudgetId(UUID budgetId);

    Optional<BudgetCategory> findByIdAndBudgetId(UUID id, UUID budgetId);

    List<BudgetCategory> findByBudget_User_IdAndBudget_Id(UUID userId, UUID budgetId);

    Optional<BudgetCategory> findByIdAndBudget_User_Id(UUID id, UUID userId);
}
