package com.trezi.financial.snapshot.repository;

import com.trezi.financial.snapshot.entity.FinancialSnapshot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.time.LocalDate;

@Repository
public interface FinancialSnapshotRepository extends JpaRepository<FinancialSnapshot, UUID> {

    List<FinancialSnapshot> findByUser_IdOrderBySnapshotDateDesc(UUID userId);
    Optional<FinancialSnapshot> findFirstByUser_IdOrderBySnapshotDateDesc(UUID userId);

    Optional<FinancialSnapshot> findByIdAndUser_Id(UUID id, UUID userId);
    Optional<FinancialSnapshot> findByUser_IdAndSnapshotDate(
        UUID userId,
        LocalDate snapshotDate
    );
}
