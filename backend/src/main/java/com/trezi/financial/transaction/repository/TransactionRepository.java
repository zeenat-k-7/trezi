package com.trezi.financial.transaction.repository;

import com.trezi.financial.transaction.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, UUID> {

    List<Transaction> findByUser_IdOrderByTransactionDateDesc(UUID userId);

    Optional<Transaction> findByIdAndUser_Id(UUID id, UUID userId);
}
