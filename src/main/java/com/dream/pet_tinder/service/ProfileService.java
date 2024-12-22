package com.dream.pet_tinder.service;

import com.dream.pet_tinder.dto.PhotosDto;
import com.dream.pet_tinder.dto.ProfileDto;

import java.io.IOException;
import java.util.List;

public interface ProfileService {

    List<ProfileDto> getUserPetsProfiles();

    ProfileDto getUserPetsProfile(Long id);

    void updateUserPetsProfile(ProfileDto newProfile, Long id);

    void createNewProfile(ProfileDto newProfile) throws IOException;

    List<PhotosDto> getProfilePhotos(Long id);
}
