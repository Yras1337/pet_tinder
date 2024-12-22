package com.dream.pet_tinder.repository;

import com.dream.pet_tinder.model.blacklist.BlackList;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlackListRepository extends JpaRepository<BlackList, Long> {

}
