package com.dream.pet_tinder.controller;

import com.dream.pet_tinder.dto.PasswordChangeDTO;
import com.dream.pet_tinder.exception.PasswordChangeException;
import com.dream.pet_tinder.exception.AuthException;
import com.dream.pet_tinder.model.user.User;
import com.dream.pet_tinder.security.AuthContextHandler;
import com.dream.pet_tinder.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/")
@AllArgsConstructor
public class AuthenticationController {

    private final AuthContextHandler authContextHandler;
    private final UserService userService;

    @GetMapping("")
    public String auth() {
        return "auth/auth";
    }

//    @GetMapping("/profile")
//    public String profile(final Model model) {
//        User user = authContextHandler.getLoggedInUser();
//        model.addAttribute("user", user);
//        model.addAttribute("result", null);
//        return "main/profile";
//    }
//
//    @PostMapping("/profile")
//    public String updateProfile(final Model model, PasswordChangeDTO passwordChangeDTO) {
//        String result = "Password changed successfully";
//        try {
//            authContextHandler.changePassword(passwordChangeDTO);
//        } catch(PasswordChangeException | AuthException e) {
//            result = e.getMessage();
//        }
//        User user = authContextHandler.getLoggedInUser();
//        model.addAttribute("user", user);
//        model.addAttribute("result", result);
//
//        return "main/profile";
//    }

    @GetMapping("/login")
    public String login() {
        return "auth/login";
    }

    @GetMapping("/register")
    public String register() {
        return "auth/register";
    }

    @PostMapping("/register")
    public String register(final User user, final Model model) {
        String result = "successful";
        try {
            userService.register(user);
        } catch(AuthException e) {
            result = e.getMessage();
        }
        if (result == "successful"){
            return "redirect:login";
        }
        model.addAttribute("result", result);
        return "auth/register";
    }
}
