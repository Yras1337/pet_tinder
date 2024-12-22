package com.dream.pet_tinder.repository;

import com.dream.pet_tinder.model.profile.Profile;
import com.dream.pet_tinder.model.viewed_profile.ViewedProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ViewedProfileRepository extends JpaRepository<ViewedProfile, Long> {
    List<ViewedProfile> findAllByFirstProfile(Profile firstProfile);
}