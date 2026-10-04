package com.trezi.conversation.service;

import com.trezi.ai.entity.*;
import com.trezi.ai.repository.AiEvidenceRepository;
import com.trezi.ai.repository.AiExecutionRepository;
import com.trezi.ai.repository.ComplianceCheckRepository;
import com.trezi.ai.repository.ReasoningTraceRepository;
import com.trezi.ai.service.AiGatewayService;
import com.trezi.assessment.entity.AssessmentResult;
import com.trezi.assessment.repository.AssessmentResultRepository;
import com.trezi.conversation.dto.*;
import com.trezi.conversation.entity.Conversation;
import com.trezi.conversation.entity.Message;
import com.trezi.conversation.entity.MessageRole;
import com.trezi.conversation.repository.ConversationRepository;
import com.trezi.conversation.repository.MessageRepository;
import com.trezi.financial.snapshot.entity.FinancialSnapshot;
import com.trezi.financial.snapshot.repository.FinancialSnapshotRepository;
import com.trezi.user.entity.User;
import com.trezi.user.entity.UserProfile;
import com.trezi.user.repository.UserProfileRepository;
import com.trezi.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ConversationService {

    private final ConversationRepository conversationRepository;
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final FinancialSnapshotRepository financialSnapshotRepository;
    private final AssessmentResultRepository assessmentResultRepository;
    private final AiGatewayService aiGatewayService;
    private final AiExecutionRepository aiExecutionRepository;
    private final AiEvidenceRepository aiEvidenceRepository;
    private final ReasoningTraceRepository reasoningTraceRepository;
    private final ComplianceCheckRepository complianceCheckRepository;

    public ConversationService(
            ConversationRepository conversationRepository,
            MessageRepository messageRepository,
            UserRepository userRepository,
            UserProfileRepository userProfileRepository,
            FinancialSnapshotRepository financialSnapshotRepository,
            AssessmentResultRepository assessmentResultRepository,
            AiGatewayService aiGatewayService,
            AiExecutionRepository aiExecutionRepository,
            AiEvidenceRepository aiEvidenceRepository,
            ReasoningTraceRepository reasoningTraceRepository,
            ComplianceCheckRepository complianceCheckRepository
    ) {
        this.conversationRepository = conversationRepository;
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
        this.userProfileRepository = userProfileRepository;
        this.financialSnapshotRepository = financialSnapshotRepository;
        this.assessmentResultRepository = assessmentResultRepository;
        this.aiGatewayService = aiGatewayService;
        this.aiExecutionRepository = aiExecutionRepository;
        this.aiEvidenceRepository = aiEvidenceRepository;
        this.reasoningTraceRepository = reasoningTraceRepository;
        this.complianceCheckRepository = complianceCheckRepository;
    }

    @Transactional(readOnly = true)
    public List<ConversationResponse> listConversations(UUID userId) {
        return conversationRepository.findByUserIdOrderByUpdatedAtDesc(userId).stream().map(c -> {
            ConversationResponse r = new ConversationResponse();
            r.setId(c.getId());
            r.setTitle(c.getTitle());
            r.setCreatedAt(c.getCreatedAt());
            r.setUpdatedAt(c.getUpdatedAt());
            // simplistic last message retrieval for demo
            List<Message> msgs = messageRepository.findByConversationIdOrderByCreatedAtAsc(c.getId());
            if (!msgs.isEmpty()) {
                r.setLastMessage(msgs.get(msgs.size() - 1).getContent());
            }
            return r;
        }).collect(Collectors.toList());
    }

    @Transactional
    public ConversationResponse createConversation(UUID userId, String title) {
        User user = userRepository.findById(userId).orElseThrow();
        Conversation c = new Conversation();
        c.setUser(user);
        c.setTitle(title);
        c = conversationRepository.save(c);

        ConversationResponse r = new ConversationResponse();
        r.setId(c.getId());
        r.setTitle(c.getTitle());
        r.setCreatedAt(c.getCreatedAt());
        r.setUpdatedAt(c.getUpdatedAt());
        return r;
    }

    @Transactional(readOnly = true)
    public List<MessageResponse> getMessages(UUID userId, UUID conversationId) {
        List<Message> msgs = messageRepository.findByConversationIdOrderByCreatedAtAsc(conversationId);
        return msgs.stream().map(m -> {
            MessageResponse r = new MessageResponse();
            r.setId(m.getId());
            r.setRole(m.getRole().name());
            r.setContent(m.getContent());
            r.setCreatedAt(m.getCreatedAt());
            return r;
        }).collect(Collectors.toList());
    }

    @Transactional
    public MessageResponse sendMessage(UUID userId, UUID conversationId, String messageText) {
        User user = userRepository.findById(userId).orElseThrow();
        Conversation conversation;
        if (conversationId == null) {
            conversation = new Conversation();
            conversation.setUser(user);
            conversation.setTitle("New Conversation");
            conversation = conversationRepository.save(conversation);
        } else {
            conversation = conversationRepository.findById(conversationId).orElseThrow();
        }

        Message userMsg = new Message();
        userMsg.setConversation(conversation);
        userMsg.setRole(MessageRole.USER);
        userMsg.setContent(messageText);
        userMsg = messageRepository.save(userMsg);

        Map<String, Object> userContext = new HashMap<>();
        Optional<UserProfile> profile = userProfileRepository.findByUser_Id(userId);
        if (profile.isPresent()) {
            userContext.put("age", profile.get().getAge());
            userContext.put("profession", profile.get().getProfession());
            userContext.put("riskPreference", profile.get().getRiskPreference());
        }
        FinancialSnapshot snapshot = financialSnapshotRepository.findFirstByUser_IdOrderBySnapshotDateDesc(userId).orElse(null);
        if (snapshot != null) {
            userContext.put("netWorth", snapshot.getNetWorth());
        }
        AssessmentResult result = assessmentResultRepository.findFirstByAssessment_User_IdOrderByCreatedAtDesc(userId).orElse(null);
        if (result != null) {
            userContext.put("literacyLevel", result.getLiteracyLevel());
        }

        List<Message> history = messageRepository.findByConversationIdOrderByCreatedAtAsc(conversation.getId());
        List<Map<String, String>> historyList = history.stream().map(h -> {
            Map<String, String> hm = new HashMap<>();
            hm.put("role", h.getRole().name().toLowerCase());
            hm.put("content", h.getContent());
            return hm;
        }).collect(Collectors.toList());

        AiChatRequest aiReq = new AiChatRequest();
        aiReq.setUserId(userId);
        aiReq.setConversationId(conversation.getId());
        aiReq.setMessage(messageText);
        aiReq.setUserContext(userContext);
        aiReq.setConversationHistory(historyList);

        AiExecution exec = new AiExecution();
        exec.setUser(user);
        exec.setConversation(conversation);
        exec.setRequestMessage(userMsg);
        exec.setExecutionStatus(ExecutionStatus.RUNNING);
        exec = aiExecutionRepository.save(exec);

        AiChatResponse aiResp;
        try {
            aiResp = aiGatewayService.chat(aiReq);
        } catch (Exception e) {
            exec.setExecutionStatus(ExecutionStatus.FAILED);
            exec.setErrorMessage(e.getMessage());
            exec.setCompletedAt(Instant.now());
            aiExecutionRepository.save(exec);
            throw new RuntimeException("AI Service error: " + e.getMessage());
        }

        exec.setExecutionStatus(ExecutionStatus.COMPLETED);
        exec.setModelProvider(aiResp.getModelProvider());
        exec.setModelName(aiResp.getModelName());
        exec.setCompletedAt(Instant.now());
        exec = aiExecutionRepository.save(exec);

        Message asstMsg = new Message();
        asstMsg.setConversation(conversation);
        asstMsg.setRole(MessageRole.ASSISTANT);
        asstMsg.setContent(aiResp.getResponse());
        asstMsg = messageRepository.save(asstMsg);

        conversationRepository.save(conversation);

        if (aiResp.getReasoningTrace() != null) {
            ReasoningTrace trace = new ReasoningTrace();
            trace.setAiExecution(exec);
            trace.setIntent(aiResp.getReasoningTrace().getIntent());
            trace.setContextSummary(aiResp.getReasoningTrace().getContextSummary());
            trace.setEvidenceSummary(aiResp.getReasoningTrace().getEvidenceSummary());
            trace.setCalculationSummary(aiResp.getReasoningTrace().getCalculationSummary());
            trace.setDecision(aiResp.getReasoningTrace().getDecision());
            reasoningTraceRepository.save(trace);
        }

        if (aiResp.getComplianceChecks() != null) {
            for (ComplianceCheckDto ccd : aiResp.getComplianceChecks()) {
                ComplianceCheck cc = new ComplianceCheck();
                cc.setAiExecution(exec);
                cc.setCheckType(ccd.getCheckType());
                if(ccd.getStatus() != null) cc.setStatus(ComplianceStatus.valueOf(ccd.getStatus()));
                if(ccd.getRiskLevel() != null) cc.setRiskLevel(RiskLevel.valueOf(ccd.getRiskLevel()));
                cc.setIssues(ccd.getIssues());
                cc.setRequiredAction(ccd.getRequiredAction());
                complianceCheckRepository.save(cc);
            }
        }

        if (aiResp.getSources() != null) {
            for(SourceDto sd : aiResp.getSources()) {
                AiEvidence aie = new AiEvidence();
                aie.setAiExecution(exec);
                aie.setKnowledgeChunkId(sd.getChunkId());
                aie.setRelevanceScore(sd.getRelevanceScore());
                aie.setEvidenceType("KNOWLEDGE_CHUNK");
                aiEvidenceRepository.save(aie);
            }
        }

        MessageResponse response = new MessageResponse();
        response.setId(asstMsg.getId());
        response.setRole(asstMsg.getRole().name());
        response.setContent(asstMsg.getContent());
        response.setCreatedAt(asstMsg.getCreatedAt());
        response.setSources(aiResp.getSources());
        response.setReasoningTrace(aiResp.getReasoningTrace());
        response.setComplianceChecks(aiResp.getComplianceChecks());

        return response;
    }
}
