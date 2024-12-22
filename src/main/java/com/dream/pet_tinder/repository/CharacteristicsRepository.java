package com.dream.pet_tinder.repository;

import com.dream.pet_tinder.model.characteristics.Characteristics;
import com.dream.pet_tinder.model.profile.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CharacteristicsRepository extends JpaRepository<Characteristics, Long> {
    List<Characteristics> findAllByProfile(Profile profile);

}