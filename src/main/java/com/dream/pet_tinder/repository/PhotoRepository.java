package com.dream.pet_tinder.repository;

import com.dream.pet_tinder.model.photo.Photo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhotoRepository extends JpaRepository<Photo, Long> {

}
