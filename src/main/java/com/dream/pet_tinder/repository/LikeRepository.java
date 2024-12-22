package com.dream.pet_tinder.repository;

import com.dream.pet_tinder.model.like.Like;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, Long> {

}
