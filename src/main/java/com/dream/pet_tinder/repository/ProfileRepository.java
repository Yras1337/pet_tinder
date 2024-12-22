package com.dream.pet_tinder.repository;

import com.dream.pet_tinder.model.profile.Profile;
import com.dream.pet_tinder.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
    List<Profile> findAllByOwner(User user);
}
