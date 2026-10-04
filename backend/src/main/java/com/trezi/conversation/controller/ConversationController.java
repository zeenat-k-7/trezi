package com.trezi.conversation.controller;

import com.trezi.conversation.dto.ConversationResponse;
import com.trezi.conversation.dto.CreateConversationRequest;
import com.trezi.conversation.dto.MessageResponse;
import com.trezi.conversation.dto.SendMessageRequest;
import com.trezi.conversation.service.ConversationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/conversations")
public class ConversationController {

    private final ConversationService conversationService;

    public ConversationController(ConversationService conversationService) {
        this.conversationService = conversationService;
    }

    private UUID getCurrentUserId(Authentication authentication) {
        return UUID.fromString(authentication.getName());
    }

    @GetMapping("/")
    public ResponseEntity<List<ConversationResponse>> listConversations(Authentication authentication) {
        return ResponseEntity.ok(conversationService.listConversations(getCurrentUserId(authentication)));
    }

    @PostMapping("/")
    public ResponseEntity<ConversationResponse> createConversation(Authentication authentication, @RequestBody CreateConversationRequest request) {
        return ResponseEntity.ok(conversationService.createConversation(getCurrentUserId(authentication), request.getTitle()));
    }

    @GetMapping("/{id}/messages")
    public ResponseEntity<List<MessageResponse>> getMessages(Authentication authentication, @PathVariable UUID id) {
        return ResponseEntity.ok(conversationService.getMessages(getCurrentUserId(authentication), id));
    }

    @PostMapping("/{id}/messages")
    public ResponseEntity<MessageResponse> sendMessage(Authentication authentication, @PathVariable UUID id, @RequestBody SendMessageRequest request) {
        return ResponseEntity.ok(conversationService.sendMessage(getCurrentUserId(authentication), id, request.getMessage()));
    }

    @PostMapping("/chat")
    public ResponseEntity<MessageResponse> chat(Authentication authentication, @RequestBody SendMessageRequest request) {
        return ResponseEntity.ok(conversationService.sendMessage(getCurrentUserId(authentication), null, request.getMessage()));
    }
}
