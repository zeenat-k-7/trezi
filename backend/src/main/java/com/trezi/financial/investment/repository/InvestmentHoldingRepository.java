package com.trezi.financial.investment.repository;

import com.trezi.financial.investment.entity.InvestmentHolding;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface InvestmentHoldingRepository extends JpaRepository<InvestmentHolding, UUID> {

    List<InvestmentHolding> findByUser_Id(UUID userId);

    Optional<InvestmentHolding> findByIdAndUser_Id(UUID id, UUID userId);
}
