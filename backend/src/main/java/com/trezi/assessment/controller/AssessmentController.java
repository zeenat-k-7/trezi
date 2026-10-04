package com.trezi.assessment.controller;

import com.trezi.assessment.dto.AssessmentResultResponse;
import com.trezi.assessment.dto.QuestionDto;
import com.trezi.assessment.dto.SubmitAssessmentRequest;
import com.trezi.assessment.service.AssessmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/assessments")
public class AssessmentController {

    private final AssessmentService assessmentService;

    public AssessmentController(AssessmentService assessmentService) {
        this.assessmentService = assessmentService;
    }

    private UUID getCurrentUserId(Authentication authentication) {
        return UUID.fromString(authentication.getName());
    }

    @GetMapping("/questions")
    public ResponseEntity<List<QuestionDto>> getQuestions() {
        return ResponseEntity.ok(assessmentService.getQuestions());
    }

    @PostMapping("/submit")
    public ResponseEntity<AssessmentResultResponse> submitAssessment(Authentication authentication, @RequestBody SubmitAssessmentRequest request) {
        return ResponseEntity.ok(assessmentService.submitAssessment(getCurrentUserId(authentication), request));
    }

    @GetMapping("/latest-result")
    public ResponseEntity<AssessmentResultResponse> getLatestResult(Authentication authentication) {
        AssessmentResultResponse result = assessmentService.getLatestResult(getCurrentUserId(authentication));
        return result != null ? ResponseEntity.ok(result) : ResponseEntity.notFound().build();
    }
}
