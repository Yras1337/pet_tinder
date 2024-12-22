package com.dream.pet_tinder.service;

import com.dream.pet_tinder.dto.ProfileDto;

public interface RecommendationsService {
    ProfileDto getNextRecommendation(Long id);
}
