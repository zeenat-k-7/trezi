package com.trezi.assessment;

import com.trezi.assessment.dto.AnswerDto;
import com.trezi.assessment.dto.AssessmentResultResponse;
import com.trezi.assessment.dto.SubmitAssessmentRequest;
import com.trezi.assessment.entity.*;
import com.trezi.assessment.repository.AssessmentQuestionRepository;
import com.trezi.assessment.repository.AssessmentRepository;
import com.trezi.assessment.repository.AssessmentResponseRepository;
import com.trezi.assessment.repository.AssessmentResultRepository;
import com.trezi.assessment.service.AssessmentService;
import com.trezi.user.entity.User;
import com.trezi.user.entity.UserRole;
import com.trezi.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AssessmentServiceTest {

    @Mock
    private AssessmentRepository assessmentRepository;
    @Mock
    private AssessmentQuestionRepository questionRepository;
    @Mock
    private AssessmentResponseRepository responseRepository;
    @Mock
    private AssessmentResultRepository resultRepository;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AssessmentService assessmentService;

    private User testUser;
    private UUID userId;
    private AssessmentQuestion q1;
    private AssessmentQuestion q2;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        testUser = new User();
        testUser.setId(userId);
        testUser.setEmail("test@trezi.edu");
        testUser.setRole(UserRole.USER);

        q1 = new AssessmentQuestion(
                "What does 50/30/20 stand for?",
                "BUDGETING",
                Difficulty.EASY,
                "50% needs, 30% wants, 20% savings"
        );
        q1.setId(UUID.randomUUID());

        q2 = new AssessmentQuestion(
                "What is compound interest?",
                "SAVING",
                Difficulty.MEDIUM,
                "Interest on principal plus accumulated interest"
        );
        q2.setId(UUID.randomUUID());
    }

    @Test
    void testSubmitAssessment_DeterministicScoring() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(testUser));
        when(assessmentRepository.save(any(Assessment.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(questionRepository.findById(q1.getId())).thenReturn(Optional.of(q1));
        when(questionRepository.findById(q2.getId())).thenReturn(Optional.of(q2));
        when(resultRepository.save(any(AssessmentResult.class))).thenAnswer(invocation -> invocation.getArgument(0));

        AnswerDto a1 = new AnswerDto();
        a1.setQuestionId(q1.getId());
        a1.setSelectedAnswer("50% needs, 30% wants, 20% savings"); // correct -> 100%

        AnswerDto a2 = new AnswerDto();
        a2.setQuestionId(q2.getId());
        a2.setSelectedAnswer("Wrong answer"); // incorrect -> 0%

        SubmitAssessmentRequest request = new SubmitAssessmentRequest();
        request.setResponses(List.of(a1, a2));

        AssessmentResultResponse response = assessmentService.submitAssessment(userId, request);

        assertNotNull(response);
        // Overall: (100 + 0) / 2 = 50.00
        assertEquals(new BigDecimal("50.00"), response.getOverallScore());
        assertEquals("INTERMEDIATE", response.getLiteracyLevel());
        assertEquals(new BigDecimal("100.00"), response.getBudgetingScore());
        assertEquals(new BigDecimal("0.00"), response.getSavingScore());

        verify(responseRepository, times(2)).save(any(AssessmentResponse.class));
        verify(resultRepository, times(1)).save(any(AssessmentResult.class));
    }
}
