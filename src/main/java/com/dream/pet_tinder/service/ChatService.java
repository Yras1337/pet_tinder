package com.dream.pet_tinder.service;

import com.dream.pet_tinder.dto.ChatDto;
import com.dream.pet_tinder.dto.MessageDto;

import java.util.List;

public interface ChatService {
    List<ChatDto> getChats(Long id);

    List<MessageDto> getChat(Long id, Long pId);

    void saveMessage(MessageDto message);
}
