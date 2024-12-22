package com.dream.pet_tinder.repository;

import com.dream.pet_tinder.model.viewed_profile.ViewedProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ViewedProfileRepository extends JpaRepository<ViewedProfile, Long> {

}