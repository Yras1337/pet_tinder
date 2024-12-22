package com.dream.pet_tinder.controller;

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
@RequestMapping("profiles/")
@AllArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    @GetMapping("")
    public String getProfiles(final Model model) {
        model.addAttribute("profiles", profileService.getUserPetsProfiles());

        return "profile/profiles";
    }

    @GetMapping("/{id}")
    public String getProfile(@PathVariable Long id, final Model model) {
        model.addAttribute("profile", profileService.getUserPetsProfile(id));

        return "profile/profile";
    }

    @PostMapping("/{id}")
    public String updateProfile(@PathVariable Long id, Profile profile, final Model model) {
        model.addAttribute("profile", profileService.updateUserPetsProfile(profile, id));

        return "profile/profile";
    }
}
