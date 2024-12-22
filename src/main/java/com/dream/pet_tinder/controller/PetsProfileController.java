package com.dream.pet_tinder.controller;

import com.dream.pet_tinder.dto.ProfileDto;
import com.dream.pet_tinder.model.profile.Profile;
import com.dream.pet_tinder.service.ProfileService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/profiles")
@AllArgsConstructor
public class PetsProfileController {

    private final ProfileService profileService;

    @GetMapping("")
    public String getProfiles(final Model model) {
        model.addAttribute("profiles", profileService.getUserPetsProfiles());

        return "pets/profiles";
    }

    @GetMapping("/new")
    public String newProfile() {
        return "pets/new_profile";
    }

    @PostMapping("/new")
    public String newProfile(final ProfileDto profile, final Model model) {
        profileService.createNewProfile(profile);
        return "pets/new_profile";
    }

    @GetMapping("/{id}")
    public String getProfile(@PathVariable Long id, final Model model) {
        model.addAttribute("profile", profileService.getUserPetsProfile(id));

        return "pets/profile";
    }

    @PostMapping("/{id}")
    public String updateProfile(@PathVariable Long id, Profile profile, final Model model) {
        model.addAttribute("profile", profileService.updateUserPetsProfile(profile, id));

        return "pets/profile";
    }
}
