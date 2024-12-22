package com.dream.pet_tinder.service;

import com.dream.pet_tinder.dto.ProfileDto;
import com.dream.pet_tinder.model.profile.Profile;

import java.util.List;

public interface ProfileService {

    List<ProfileDto> getUserPetsProfiles();

    Profile getUserPetsProfile(Long id);

    Profile updateUserPetsProfile(Profile profile, Long id);

    void createNewProfile(ProfileDto newProfile);
}
