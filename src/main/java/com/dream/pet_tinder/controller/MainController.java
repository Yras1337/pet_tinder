package com.dream.pet_tinder.controller;

import com.dream.pet_tinder.exception.UserNotLoggedInException;
import com.dream.pet_tinder.model.user.User;
import com.dream.pet_tinder.security.AuthContextHandler;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/")
@AllArgsConstructor
public class MainController {
    private final AuthContextHandler authContextHandler;

    @GetMapping
    public String mainPage() {
        try {
            User user = authContextHandler.getLoggedInUser();
            if (user == null) {
                return "main/main";
            } else {
                return "main/profile";
            }
        } catch (UserNotLoggedInException e) {
            return "main/main";
        }
    }

    @GetMapping("profile")
    public String profile(final Model model) {
        User user = authContextHandler.getLoggedInUser();
        model.addAttribute("user", user);
        model.addAttribute("result", null);
        return "main/profile";
    }

//    @PostMapping("/profile")
//    public String updateProfile(final Model model, PasswordChangeDTO passwordChangeDTO) throws PasswordChangeException, UserIsNotLoggedInException {
//        String result = "Password changed successfully";
//        try {
//            authContextHandler.changePassword(passwordChangeDTO);
//        } catch(PasswordChangeException | UserIsNotLoggedInException e) {
//            result = e.getMessage();
//        }
//        User user = authContextHandler.getLoggedInUser();
//        model.addAttribute("user", user);
//        model.addAttribute("result", result);
//
//        return "main/profile";
//    }
}
