package com.dream.pet_tinder.controller;

import com.dream.pet_tinder.dto.MessageDto;
import com.dream.pet_tinder.service.ChatService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
@AllArgsConstructor
public class MessageController {

    private final ChatService chatService;

    @GetMapping("/{sender}/{recipient}")
    public List<MessageDto> getMessages(
            @PathVariable Long sender,
            @PathVariable Long recipient) {
        return chatService.getChat(sender, recipient);
    }

    @PostMapping
    public void sendMessage(@RequestBody MessageDto message) {
        chatService.saveMessage(message);
    }
}
