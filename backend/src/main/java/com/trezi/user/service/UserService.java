package com.trezi.user.service;

import com.trezi.user.dto.ProfileRequest;
import com.trezi.user.dto.ProfileResponse;
import com.trezi.user.entity.InvestmentExperience;
import com.trezi.user.entity.RiskPreference;
import com.trezi.user.entity.User;
import com.trezi.user.entity.UserProfile;
import com.trezi.user.repository.UserProfileRepository;
import com.trezi.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;

    public UserService(UserRepository userRepository, UserProfileRepository userProfileRepository) {
        this.userRepository = userRepository;
        this.userProfileRepository = userProfileRepository;
    }

    @Transactional(readOnly = true)
    public ProfileResponse getProfile(UUID userId) {
        return userProfileRepository.findByUser_Id(userId)
                .map(this::mapToResponse)
                .orElse(null);
    }

    @Transactional
    public ProfileResponse createOrUpdateProfile(UUID userId, ProfileRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        UserProfile profile = userProfileRepository.findByUser_Id(userId)
                .orElseGet(() -> {
                    UserProfile p = new UserProfile();
                    p.setUser(user);
                    p.setCreatedAt(Instant.now());
                    return p;
                });

        if(request.getAge() != null) profile.setAge(request.getAge());
        if(request.getProfession() != null) profile.setProfession(request.getProfession());
        if(request.getEmploymentType() != null) profile.setEmploymentType(request.getEmploymentType());
        if(request.getDependentsCount() != null) profile.setDependentsCount(request.getDependentsCount());
        if(request.getPrimaryFinancialGoal() != null) profile.setPrimaryFinancialGoal(request.getPrimaryFinancialGoal());

        if (request.getRiskPreference() != null) {
            profile.setRiskPreference(RiskPreference.valueOf(request.getRiskPreference()));
        }
        if (request.getInvestmentExperience() != null) {
            profile.setInvestmentExperience(InvestmentExperience.valueOf(request.getInvestmentExperience()));
        }

        if(request.getFinancialKnowledgeRating() != null) profile.setFinancialKnowledgeRating(request.getFinancialKnowledgeRating());
        if(request.getPreferredLanguage() != null) profile.setPreferredLanguage(request.getPreferredLanguage());
        if(request.getPreferredExplanationStyle() != null) profile.setPreferredExplanationStyle(request.getPreferredExplanationStyle());
        profile.setUpdatedAt(Instant.now());

        profile = userProfileRepository.save(profile);
        return mapToResponse(profile);
    }

    private ProfileResponse mapToResponse(UserProfile profile) {
        ProfileResponse r = new ProfileResponse();
        r.setId(profile.getId());
        r.setUserId(profile.getUser().getId());
        r.setEmail(profile.getUser().getEmail());
        r.setAge(profile.getAge());
        r.setProfession(profile.getProfession());
        r.setEmploymentType(profile.getEmploymentType());
        r.setDependentsCount(profile.getDependentsCount());
        r.setPrimaryFinancialGoal(profile.getPrimaryFinancialGoal());
        r.setRiskPreference(profile.getRiskPreference() != null ? profile.getRiskPreference().name() : null);
        r.setInvestmentExperience(profile.getInvestmentExperience() != null ? profile.getInvestmentExperience().name() : null);
        r.setFinancialKnowledgeRating(profile.getFinancialKnowledgeRating());
        r.setPreferredLanguage(profile.getPreferredLanguage());
        r.setPreferredExplanationStyle(profile.getPreferredExplanationStyle());
        return r;
    }
}
