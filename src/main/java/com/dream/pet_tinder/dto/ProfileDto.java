package com.dream.pet_tinder.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfileDto {
    private String id;
    private String name;
    private String type;
    private String country;
    private String city;
    private String description;
    private List<String> custom;
    private byte[] mainPhoto;
    private List<byte[]> photos;
    private String albumPhoto;
}
