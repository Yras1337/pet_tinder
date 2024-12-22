package com.dream.pet_tinder.service.impl;

import com.dream.pet_tinder.model.profile.Profile;
import com.dream.pet_tinder.model.user.User;
import com.dream.pet_tinder.repository.ProfileRepository;
import com.dream.pet_tinder.security.AuthContextHandler;
import com.dream.pet_tinder.service.ProfileService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final ProfileRepository profileRepository;
    private final AuthContextHandler authContextHandler;

    @Override
    public List<Profile> getUserPetsProfiles() {
        User user = authContextHandler.getLoggedInUser();

        return profileRepository.findAllByOwner(user);
    }

    @Override
    public Profile getUserPetsProfile(Long id) {
        return profileRepository.getById(id);
    }

    @Override
    public Profile updateUserPetsProfile(Profile profile, Long id) {
        Profile currentProfile = getUserPetsProfile(id);
        currentProfile.setAddress(profile.getAddress());
        currentProfile.setDescription(profile.getDescription());

        Profile newFather = profileRepository.getById(profile.getFather().getId());
        Profile newMother = profileRepository.getById(profile.getMother().getId());

        currentProfile.setFather(newFather);
        currentProfile.setMother(newMother);

        return currentProfile;
    }
}
