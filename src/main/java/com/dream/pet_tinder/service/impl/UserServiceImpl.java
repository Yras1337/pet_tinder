package com.dream.pet_tinder.service.impl;

import com.dream.pet_tinder.exception.AuthException;
import com.dream.pet_tinder.model.user.Status;
import com.dream.pet_tinder.model.user.User;
import com.dream.pet_tinder.repository.UserRepository;
import com.dream.pet_tinder.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public void register(User user) {
        if (userRepository.findByEmail(user.getEmail()) != null) {
            throw new AuthException("User already exists");
        }

        user.setStatus(Status.ACTIVE);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }
}
