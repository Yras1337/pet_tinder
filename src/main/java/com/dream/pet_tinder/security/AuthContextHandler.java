package com.dream.pet_tinder.security;

import com.dream.pet_tinder.dto.PasswordChangeDTO;
import com.dream.pet_tinder.exception.PasswordChangeException;
import com.dream.pet_tinder.exception.AuthException;
import com.dream.pet_tinder.model.user.User;
import com.dream.pet_tinder.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@AllArgsConstructor
public class AuthContextHandler {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;

    public User getLoggedInUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email);

        if (user == null) {
            throw new AuthException();
        } else {
            return user;
        }
    }


    public void changePassword(PasswordChangeDTO passwordChangeDTO) {
        if (!Objects.equals(passwordChangeDTO.getNewPassword(), passwordChangeDTO.getNewPasswordConfirmation())) {
            throw new PasswordChangeException("New passwords are different");
        }

        User user = getLoggedInUser();
        if (!encoder.matches(passwordChangeDTO.getOldPassword(), user.getPassword())) {
            throw new PasswordChangeException("Old password is wrong");
        }
        user.setPassword(encoder.encode(passwordChangeDTO.getNewPassword()));
        userRepository.save(user);
    }
}
