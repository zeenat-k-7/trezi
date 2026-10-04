package com.trezi.conversation.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class AiChatRequest {
    @JsonProperty("user_id")
    private UUID userId;

    @JsonProperty("message")
    private String message;

    @JsonProperty("conversation_id")
    private UUID conversationId;

    @JsonProperty("conversation_history")
    private List<Map<String, String>> conversationHistory;

    @JsonProperty("user_context")
    private Map<String, Object> userContext;

    // Getters and Setters
    public UUID getUserId() { return userId; }
    public void setUserId(UUID userId) { this.userId = userId; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public UUID getConversationId() { return conversationId; }
    public void setConversationId(UUID conversationId) { this.conversationId = conversationId; }
    public List<Map<String, String>> getConversationHistory() { return conversationHistory; }
    public void setConversationHistory(List<Map<String, String>> conversationHistory) { this.conversationHistory = conversationHistory; }
    public Map<String, Object> getUserContext() { return userContext; }
    public void setUserContext(Map<String, Object> userContext) { this.userContext = userContext; }
}
