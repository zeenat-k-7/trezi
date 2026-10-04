package com.trezi.ai.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trezi.conversation.dto.AiChatRequest;
import com.trezi.conversation.dto.AiChatResponse;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Service
public class AiGatewayService {

    private final RestClient aiServiceRestClient;
    private final ObjectMapper objectMapper;

    public AiGatewayService(
            RestClient aiServiceRestClient,
            ObjectMapper objectMapper
    ) {
        this.aiServiceRestClient = aiServiceRestClient;
        this.objectMapper = objectMapper;
    }

    public AiChatResponse chat(AiChatRequest request) {

        ServletRequestAttributes attributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        String authHeader = null;

        if (attributes != null) {
            authHeader = attributes.getRequest().getHeader("Authorization");
        }

        System.out.println("========== AI GATEWAY ==========");
        System.out.println("User ID: " + request.getUserId());
        System.out.println("Message: " + request.getMessage());
        System.out.println("Conversation ID: " + request.getConversationId());
        System.out.println("Authorization present: "
                + (authHeader != null && !authHeader.isBlank()));

        // Explicitly serialize the request to JSON
        final String jsonBody;

        try {
            jsonBody = objectMapper.writeValueAsString(request);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize AI chat request", e);
        }

        System.out.println("JSON sent to AI service:");
        System.out.println(jsonBody);
        System.out.println("================================");

        var requestSpec = aiServiceRestClient
                .post()
                .uri("/api/chat")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(jsonBody);

        if (authHeader != null && !authHeader.isBlank()) {
            requestSpec.header("Authorization", authHeader);
        }

        return requestSpec
                .retrieve()
                .body(AiChatResponse.class);
    }
}