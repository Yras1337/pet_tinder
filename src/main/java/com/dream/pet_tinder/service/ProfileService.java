package com.dream.pet_tinder.service;

import com.dream.pet_tinder.model.profile.Profile;

import java.util.List;

public interface ProfileService {

    List<Profile> getUserPetsProfiles();

    Profile getUserPetsProfile(Long id);

    Profile updateUserPetsProfile(Profile profile, Long id);
}
