package com.dream.pet_tinder.repository;

import com.dream.pet_tinder.model.user.Role;
import com.dream.pet_tinder.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findUsersByRole(Role role);
    User findByEmail(String email);

    boolean existsByEmail(String email);
}