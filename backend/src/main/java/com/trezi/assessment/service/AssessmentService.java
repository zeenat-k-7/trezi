package com.trezi.assessment.service;

import com.trezi.assessment.dto.AnswerDto;
import com.trezi.assessment.dto.AssessmentResultResponse;
import com.trezi.assessment.dto.QuestionDto;
import com.trezi.assessment.dto.SubmitAssessmentRequest;
import com.trezi.assessment.entity.*;
import com.trezi.assessment.repository.*;
import com.trezi.user.entity.User;
import com.trezi.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AssessmentService {

    private final AssessmentRepository assessmentRepository;
    private final AssessmentQuestionRepository questionRepository;
    private final AssessmentResponseRepository responseRepository;
    private final AssessmentResultRepository resultRepository;
    private final UserRepository userRepository;

    public AssessmentService(AssessmentRepository assessmentRepository, AssessmentQuestionRepository questionRepository, AssessmentResponseRepository responseRepository, AssessmentResultRepository resultRepository, UserRepository userRepository) {
        this.assessmentRepository = assessmentRepository;
        this.questionRepository = questionRepository;
        this.responseRepository = responseRepository;
        this.resultRepository = resultRepository;
        this.userRepository = userRepository;
    }

    public List<QuestionDto> getQuestions() {
        return questionRepository.findAll().stream().map(q -> {
            QuestionDto dto = new QuestionDto();
            dto.setId(q.getId());
            dto.setQuestionText(q.getQuestionText());
            dto.setTopic(q.getTopic());
            dto.setDifficulty(q.getDifficulty().name());
            dto.setOptions(buildOptions(q.getCorrectAnswer()));
            return dto;
        }).collect(Collectors.toList());
    }

    /** Returns 4 shuffled options including the correct answer. */
    private static List<String> buildOptions(String correctAnswer) {
        Map<String, List<String>> distractors = new HashMap<>();
        distractors.put("20%",                          Arrays.asList("10%", "30%", "50%"));
        distractors.put("Rent and utilities",            Arrays.asList("Dining out", "Travel subscriptions", "Movie tickets"));
        distractors.put("₹18,000",                      Arrays.asList("₹12,000", "₹30,000", "₹6,000"));
        distractors.put("3 to 6 months",                Arrays.asList("1 month", "1 to 2 months", "12 months"));
        distractors.put("A liquid savings account",     Arrays.asList("Stock market", "Locked FD", "Crypto exchange"));
        distractors.put("Time Value of Money",          Arrays.asList("Compound Interest", "Inflation", "Exchange Rate"));
        distractors.put("Systematic Investment Plan",   Arrays.asList("Systematic Interest Plan", "Safe Investment Policy", "Structured Investment Portfolio"));
        distractors.put("Equity fund",                  Arrays.asList("Debt fund", "Liquid fund", "Gold ETF"));
        distractors.put("SIP (Systematic Investment Plan)", Arrays.asList("FD (Fixed Deposit)", "NPS (National Pension System)", "PPF (Public Provident Fund)"));
        distractors.put("Higher risk",                  Arrays.asList("Lower risk", "Zero risk", "No relationship"));
        distractors.put("Low risk, low return",         Arrays.asList("High risk, high return", "Low risk, high return", "No risk, no return"));
        distractors.put("Conservative",                 Arrays.asList("Aggressive", "Speculative", "Balanced-aggressive"));
        distractors.put("Unified Payments Interface",   Arrays.asList("Universal Payment Index", "Unique Payment Infrastructure", "Unified Purchase Interface"));
        distractors.put("Never share your OTP with anyone", Arrays.asList("Use the same password everywhere", "Save your PIN in a notes app", "Share OTP only with your bank"));
        distractors.put("Your creditworthiness",        Arrays.asList("Your net worth", "Your tax liability", "Your investment returns"));
        distractors.put("₹1.5 lakh",                   Arrays.asList("₹1 lakh", "₹2 lakh", "₹50,000"));
        distractors.put("3 years",                      Arrays.asList("1 year", "5 years", "No lock-in"));
        distractors.put("Short-term capital gains from stocks", Arrays.asList("PPF contributions", "ELSS investments", "Life insurance premiums"));

        List<String> opts = new ArrayList<>(distractors.getOrDefault(correctAnswer, Arrays.asList("Option A", "Option B", "Option C")));
        opts.add(correctAnswer);
        Collections.shuffle(opts);
        return opts;
    }

    @Transactional
    public AssessmentResultResponse submitAssessment(UUID userId, SubmitAssessmentRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Assessment assessment = new Assessment();
        assessment.setUser(user);
        assessment.setAssessmentType("FINANCIAL_LITERACY");
        assessment.setCreatedAt(Instant.now());
        assessment = assessmentRepository.save(assessment);

        Map<String, int[]> topicScores = new HashMap<>(); // index 0: correct, index 1: total

        for (AnswerDto answer : request.getResponses()) {
            AssessmentQuestion q = questionRepository.findById(answer.getQuestionId())
                    .orElseThrow(() -> new EntityNotFoundException("Question not found"));

            boolean isCorrect = q.getCorrectAnswer().equalsIgnoreCase(answer.getSelectedAnswer());

            AssessmentResponse resp = new AssessmentResponse();
            resp.setAssessment(assessment);
            resp.setQuestion(q);
            resp.setSelectedAnswer(answer.getSelectedAnswer());
            resp.setIsCorrect(isCorrect);
            resp.setCreatedAt(Instant.now());
            responseRepository.save(resp);

            topicScores.putIfAbsent(q.getTopic(), new int[]{0, 0});
            topicScores.get(q.getTopic())[1]++;
            if (isCorrect) {
                topicScores.get(q.getTopic())[0]++;
            }
        }

        AssessmentResult result = new AssessmentResult();
        result.setAssessment(assessment);
        result.setCreatedAt(Instant.now());
        result.setUpdatedAt(Instant.now());

        BigDecimal totalOverall = BigDecimal.ZERO;
        int topicCount = 0;

        for (Map.Entry<String, int[]> entry : topicScores.entrySet()) {
            String topic = entry.getKey();
            int correct = entry.getValue()[0];
            int total = entry.getValue()[1];
            BigDecimal score = total == 0 ? BigDecimal.ZERO : BigDecimal.valueOf((double) correct / total * 100).setScale(2, RoundingMode.HALF_UP);

            switch (topic.toUpperCase()) {
                case "BUDGETING": result.setBudgetingScore(score); break;
                case "SAVING": result.setSavingScore(score); break;
                case "INVESTING": result.setInvestmentScore(score); break;
                case "RISK": result.setRiskScore(score); break;
                case "DIGITAL_FINANCE": result.setDigitalFinanceScore(score); break;
                case "TAX": result.setTaxScore(score); break;
            }
            totalOverall = totalOverall.add(score);
            topicCount++;
        }

        BigDecimal overall = topicCount == 0 ? BigDecimal.ZERO : totalOverall.divide(BigDecimal.valueOf(topicCount), 2, RoundingMode.HALF_UP);
        result.setOverallScore(overall);

        if (overall.compareTo(BigDecimal.valueOf(40)) < 0) {
            result.setLiteracyLevel(LiteracyLevel.BEGINNER);
        } else if (overall.compareTo(BigDecimal.valueOf(70)) <= 0) {
            result.setLiteracyLevel(LiteracyLevel.INTERMEDIATE);
        } else {
            result.setLiteracyLevel(LiteracyLevel.ADVANCED);
        }

        result = resultRepository.save(result);

        assessment.setCompletedAt(Instant.now());
        assessmentRepository.save(assessment);

        return mapToResultResponse(result);
    }

    @Transactional(readOnly = true)
    public AssessmentResultResponse getLatestResult(UUID userId) {
        AssessmentResult result = resultRepository.findFirstByAssessment_User_IdOrderByCreatedAtDesc(userId).orElse(null);
        if (result == null) return null;
        return mapToResultResponse(result);
    }

    private AssessmentResultResponse mapToResultResponse(AssessmentResult result) {
        AssessmentResultResponse r = new AssessmentResultResponse();
        r.setAssessmentId(result.getAssessment().getId());
        r.setOverallScore(result.getOverallScore());
        r.setBudgetingScore(result.getBudgetingScore());
        r.setSavingScore(result.getSavingScore());
        r.setInvestmentScore(result.getInvestmentScore());
        r.setRiskScore(result.getRiskScore());
        r.setDigitalFinanceScore(result.getDigitalFinanceScore());
        r.setTaxScore(result.getTaxScore());
        r.setLiteracyLevel(result.getLiteracyLevel().name());
        r.setCompletedAt(result.getAssessment().getCompletedAt());
        return r;
    }
}
