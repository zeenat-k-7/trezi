package com.trezi.dashboard.controller;

import com.trezi.assessment.entity.AssessmentResult;
import com.trezi.assessment.repository.AssessmentResultRepository;
import com.trezi.conversation.dto.ConversationResponse;
import com.trezi.conversation.repository.ConversationRepository;
import com.trezi.dashboard.dto.DashboardResponse;
import com.trezi.financial.snapshot.entity.FinancialSnapshot;
import com.trezi.financial.snapshot.repository.FinancialSnapshotRepository;
import com.trezi.user.entity.User;
import com.trezi.user.repository.UserProfileRepository;
import com.trezi.user.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final FinancialSnapshotRepository financialSnapshotRepository;
    private final AssessmentResultRepository assessmentResultRepository;
    private final ConversationRepository conversationRepository;

    public DashboardController(UserRepository userRepository, UserProfileRepository userProfileRepository, FinancialSnapshotRepository financialSnapshotRepository, AssessmentResultRepository assessmentResultRepository, ConversationRepository conversationRepository) {
        this.userRepository = userRepository;
        this.userProfileRepository = userProfileRepository;
        this.financialSnapshotRepository = financialSnapshotRepository;
        this.assessmentResultRepository = assessmentResultRepository;
        this.conversationRepository = conversationRepository;
    }

    private UUID getCurrentUserId(Authentication authentication) {
        return UUID.fromString(authentication.getName());
    }

    @GetMapping("/")
    public ResponseEntity<DashboardResponse> getDashboard(Authentication authentication) {
        UUID userId = getCurrentUserId(authentication);
        User user = userRepository.findById(userId).orElseThrow();

        DashboardResponse response = new DashboardResponse();
        response.setEmail(user.getEmail());

        response.setHasProfile(userProfileRepository.findByUser_Id(userId).isPresent());

        FinancialSnapshot snapshot = financialSnapshotRepository.findFirstByUser_IdOrderBySnapshotDateDesc(userId).orElse(null);
        if (snapshot != null) {
            response.setHasFinancialData(true);
            response.setMonthlyIncome(snapshot.getMonthlyIncome());
            response.setMonthlyExpenses(snapshot.getMonthlyExpenses());
            response.setTotalSavings(snapshot.getTotalSavings());
            response.setNetWorth(snapshot.getNetWorth());
        } else {
            response.setHasFinancialData(false);
        }

        AssessmentResult assessment = assessmentResultRepository.findFirstByAssessment_User_IdOrderByCreatedAtDesc(userId).orElse(null);
        if (assessment != null) {
            response.setHasAssessment(true);
            response.setLiteracyLevel(assessment.getLiteracyLevel().name());
            response.setOverallScore(assessment.getOverallScore());
        } else {
            response.setHasAssessment(false);
        }

        var convos = conversationRepository.findByUserIdOrderByUpdatedAtDesc(userId);
        response.setConversationCount(convos.size());

        List<ConversationResponse> recents = convos.stream().limit(5).map(c -> {
            ConversationResponse cr = new ConversationResponse();
            cr.setId(c.getId());
            cr.setTitle(c.getTitle());
            cr.setCreatedAt(c.getCreatedAt());
            cr.setUpdatedAt(c.getUpdatedAt());
            return cr;
        }).collect(Collectors.toList());

        response.setRecentConversations(recents);

        return ResponseEntity.ok(response);
    }
}
