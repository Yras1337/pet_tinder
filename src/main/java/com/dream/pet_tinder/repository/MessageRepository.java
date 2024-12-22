package com.dream.pet_tinder.repository;

import com.dream.pet_tinder.model.message.Message;
import com.dream.pet_tinder.model.photo.Photo;
import com.dream.pet_tinder.model.profile.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findAllByFirstProfile(Profile profile);
    List<Message> findAllBySecondProfile(Profile profile);
}

