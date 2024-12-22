package com.dream.pet_tinder.service;

import com.dream.pet_tinder.dto.ProfileDto;

public interface RecommendationsService {
    ProfileDto getNextRecommendation(Long id);
    void processMatch(Long id, Long pId, String flag);
    ProfileDto getPotentialRecommendation(Long id);
}
