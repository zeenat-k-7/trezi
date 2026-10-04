package com.trezi.financial.snapshot.service;

import com.trezi.financial.snapshot.dto.SnapshotRequest;
import com.trezi.financial.snapshot.dto.SnapshotResponse;
import com.trezi.financial.snapshot.entity.FinancialSnapshot;
import com.trezi.financial.snapshot.repository.FinancialSnapshotRepository;
import com.trezi.user.entity.User;
import com.trezi.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
public class FinancialSnapshotService {
    private final FinancialSnapshotRepository snapshotRepository;
    private final UserRepository userRepository;

    public FinancialSnapshotService(FinancialSnapshotRepository snapshotRepository, UserRepository userRepository) {
        this.snapshotRepository = snapshotRepository;
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public SnapshotResponse getLatestSnapshot(UUID userId) {
        FinancialSnapshot snapshot = snapshotRepository.findFirstByUser_IdOrderBySnapshotDateDesc(userId).orElse(null);
        if (snapshot == null) return null;
        return mapToResponse(snapshot);
    }

@Transactional
public SnapshotResponse createSnapshot(UUID userId, SnapshotRequest request) {
    User user = userRepository.findById(userId)
            .orElseThrow(() -> new EntityNotFoundException("User not found"));

    FinancialSnapshot snapshot = snapshotRepository
            .findByUser_IdAndSnapshotDate(userId, request.getSnapshotDate())
            .orElseGet(FinancialSnapshot::new);

    snapshot.setUser(user);
    snapshot.setSnapshotDate(request.getSnapshotDate());
    snapshot.setMonthlyIncome(request.getMonthlyIncome());
    snapshot.setMonthlyExpenses(request.getMonthlyExpenses());
    snapshot.setTotalSavings(request.getTotalSavings());
    snapshot.setTotalDebt(request.getTotalDebt());
    snapshot.setTotalInvestments(request.getTotalInvestments());
    snapshot.setNetWorth(request.getNetWorth());

    if (snapshot.getCreatedAt() == null) {
        snapshot.setCreatedAt(Instant.now());
    }

    snapshot = snapshotRepository.save(snapshot);

    return mapToResponse(snapshot);
}

    private SnapshotResponse mapToResponse(FinancialSnapshot snapshot) {
        SnapshotResponse r = new SnapshotResponse();
        r.setId(snapshot.getId());
        r.setUserId(snapshot.getUser().getId());
        r.setSnapshotDate(snapshot.getSnapshotDate());
        r.setMonthlyIncome(snapshot.getMonthlyIncome());
        r.setMonthlyExpenses(snapshot.getMonthlyExpenses());
        r.setTotalSavings(snapshot.getTotalSavings());
        r.setTotalDebt(snapshot.getTotalDebt());
        r.setTotalInvestments(snapshot.getTotalInvestments());
        r.setNetWorth(snapshot.getNetWorth());
        r.setCreatedAt(snapshot.getCreatedAt());
        return r;
    }
}
