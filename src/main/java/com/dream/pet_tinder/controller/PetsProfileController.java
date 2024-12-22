package com.dream.pet_tinder.controller;

import com.dream.pet_tinder.dto.ProfileDto;
import com.dream.pet_tinder.service.ProfileService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

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
    public String newProfile(final ProfileDto profile, final Model model) throws IOException {
        profileService.createNewProfile(profile);
        return "redirect:/profiles";
    }

    @GetMapping("/{id}")
    public String getProfile(@PathVariable Long id, final Model model) {
        model.addAttribute("old_profile", profileService.getUserPetsProfile(id));

        return "pets/profile";
    }

    @PostMapping("/{id}")
    public String updateProfile(@PathVariable Long id, ProfileDto profile, final Model model) throws IOException {
        profileService.updateUserPetsProfile(profile, id);

        return "redirect:/profiles/" + id.toString();
    }

    @GetMapping("/{id}/photos")
    public String getGallery(@PathVariable Long id, final Model model) {
        model.addAttribute("photos", profileService.getProfilePhotos(id));
        model.addAttribute("id", id);

        return "pets/photos";
    }

    @PostMapping("/{id}/photos/delete/{pId}")
    public String deletePhoto(@PathVariable Long id, @PathVariable Long pId) {
        profileService.deletePhoto(pId);

        return "redirect:/profiles/" + id.toString() + "/photos";
    }
}
