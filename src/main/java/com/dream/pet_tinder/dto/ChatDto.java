package com.dream.pet_tinder.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatDto {
    private Long id;
    private String photo;
    private String lastMessage;
    private Long messageCount;
    private Long time;
}
