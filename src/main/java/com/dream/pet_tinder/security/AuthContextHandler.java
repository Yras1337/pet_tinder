package com.dream.pet_tinder.security;

import com.dream.pet_tinder.exception.UserNotLoggedInException;
import com.dream.pet_tinder.model.user.User;
import com.dream.pet_tinder.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class AuthContextHandler {
    private final UserRepository userRepository;
    public User getLoggedInUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email);

        if (user == null) {
            throw new UserNotLoggedInException();
        } else {
            return user;
        }
    }
}
