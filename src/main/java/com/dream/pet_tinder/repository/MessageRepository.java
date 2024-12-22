package com.dream.pet_tinder.repository;

import com.dream.pet_tinder.model.message.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {

}

