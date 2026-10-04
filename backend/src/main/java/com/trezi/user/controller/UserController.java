package com.trezi.user.controller;

import com.trezi.user.dto.ProfileRequest;
import com.trezi.user.dto.ProfileResponse;
import com.trezi.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    private UUID getCurrentUserId(Authentication authentication) {
        return UUID.fromString(authentication.getName());
    }

    @GetMapping("/me/profile")
    public ResponseEntity<ProfileResponse> getProfile(Authentication authentication) {
        UUID userId = getCurrentUserId(authentication);
        ProfileResponse profile = userService.getProfile(userId);
        return profile != null ? ResponseEntity.ok(profile) : ResponseEntity.notFound().build();
    }

    @PutMapping("/me/profile")
    public ResponseEntity<ProfileResponse> createOrUpdateProfile(Authentication authentication, @RequestBody ProfileRequest request) {
        UUID userId = getCurrentUserId(authentication);
        return ResponseEntity.ok(userService.createOrUpdateProfile(userId, request));
    }
}
