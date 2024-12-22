package com.dream.pet_tinder.repository;

import com.dream.pet_tinder.model.characteristics.Characteristics;
import com.dream.pet_tinder.model.photo.Photo;
import com.dream.pet_tinder.model.profile.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PhotoRepository extends JpaRepository<Photo, Long> {
    List<Photo> findAllByProfile(Profile profile);
}
