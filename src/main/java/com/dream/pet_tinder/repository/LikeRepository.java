package com.dream.pet_tinder.repository;

import com.dream.pet_tinder.model.characteristics.Characteristics;
import com.dream.pet_tinder.model.like.Like;
import com.dream.pet_tinder.model.profile.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LikeRepository extends JpaRepository<Like, Long> {
    List<Like> findAllBySecondProfile(Profile profile);
    List<Like> findAllByFirstProfile(Profile profile);
}
