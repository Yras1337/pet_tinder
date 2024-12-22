package com.dream.pet_tinder.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfileDto {
    private Long id;
    private String name;
    private String type;
    private String country;
    private String city;
    private String description;
    private List<String> custom;
    private MultipartFile mainPhoto;
    private List<MultipartFile> photos;
    private String albumPhoto;
    private String outCustom;
}
