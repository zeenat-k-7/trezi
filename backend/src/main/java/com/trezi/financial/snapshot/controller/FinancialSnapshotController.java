package com.trezi.financial.snapshot.controller;

import com.trezi.financial.snapshot.dto.SnapshotRequest;
import com.trezi.financial.snapshot.dto.SnapshotResponse;
import com.trezi.financial.snapshot.service.FinancialSnapshotService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/financial")
public class FinancialSnapshotController {

    private final FinancialSnapshotService snapshotService;

    public FinancialSnapshotController(FinancialSnapshotService snapshotService) {
        this.snapshotService = snapshotService;
    }

    private UUID getCurrentUserId(Authentication authentication) {
        return UUID.fromString(authentication.getName());
    }

    @GetMapping("/snapshot/latest")
    public ResponseEntity<SnapshotResponse> getLatestSnapshot(Authentication authentication) {
        SnapshotResponse response = snapshotService.getLatestSnapshot(getCurrentUserId(authentication));
        return response != null ? ResponseEntity.ok(response) : ResponseEntity.notFound().build();
    }

    @PostMapping("/snapshot")
    public ResponseEntity<SnapshotResponse> createSnapshot(Authentication authentication, @RequestBody SnapshotRequest request) {
        return ResponseEntity.ok(snapshotService.createSnapshot(getCurrentUserId(authentication), request));
    }
}
