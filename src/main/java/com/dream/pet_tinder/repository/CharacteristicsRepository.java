package com.dream.pet_tinder.repository;

import com.dream.pet_tinder.model.characteristics.Characteristics;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CharacteristicsRepository extends JpaRepository<Characteristics, Long> {

}