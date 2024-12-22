package com.dream.pet_tinder.controller;

import com.dream.pet_tinder.dto.ChatDto;
import com.dream.pet_tinder.dto.MessageDto;
import com.dream.pet_tinder.model.message.Message;
import com.dream.pet_tinder.service.ChatService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/chats")
@AllArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @GetMapping("/{id}")
    public String getChats(@PathVariable Long id, final Model model) {
        model.addAttribute("chats", chatService.getChats(id));
        model.addAttribute("id", id);

        return "chat/chats";
    }

    @GetMapping("/{id}/{pId}")
    public String getChat(@PathVariable Long id, @PathVariable Long pId, final Model model) {
        model.addAttribute("sender", id);
        model.addAttribute("recipient", pId);

        return "chat/chat";
    }
}
