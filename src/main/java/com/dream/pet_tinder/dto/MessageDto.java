package com.dream.pet_tinder.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageDto {
    private String message;
    private Long sender;
    private Long recipient;
    private String author;
}