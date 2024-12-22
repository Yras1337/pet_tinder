package com.dream.pet_tinder.model.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    private User activeUser;
    private User bannedUser;

    @BeforeEach
    void setUp() {
        activeUser = new User(
                1L,
                Status.ACTIVE,
                Role.USER,
                "active@example.com",
                "password",
                "Active User"
        );

        bannedUser = new User(
                2L,
                Status.BANNED,
                Role.USER,
                "banned@example.com",
                "password",
                "Banned User"
        );
    }

    @Test
    void testIsAccountNonExpired() {
        assertTrue(activeUser.isAccountNonExpired());
        assertFalse(bannedUser.isAccountNonExpired());
    }

    @Test
    void testIsAccountNonLocked() {
        assertTrue(activeUser.isAccountNonLocked());
        assertFalse(bannedUser.isAccountNonLocked());
    }

    @Test
    void testIsCredentialsNonExpired() {
        assertTrue(activeUser.isCredentialsNonExpired());
        assertFalse(bannedUser.isCredentialsNonExpired());
    }

    @Test
    void testIsEnabled() {
        assertTrue(activeUser.isEnabled());
        assertFalse(bannedUser.isEnabled());
    }

    @Test
    void testGetAuthorities() {
        Collection<? extends GrantedAuthority> authorities = activeUser.getAuthorities();
        assertNotNull(authorities);
        assertEquals(1, authorities.size());
        assertEquals("use", authorities.iterator().next().getAuthority());
    }

    @Test
    void testGetUserDetails() {
        UserDetails userDetails = activeUser.getUserDetails();
        assertEquals(activeUser.getUsername(), userDetails.getUsername());
        assertEquals(activeUser.getPassword(), userDetails.getPassword());
        assertTrue(userDetails.isAccountNonExpired());
        assertTrue(userDetails.isAccountNonLocked());
        assertTrue(userDetails.isCredentialsNonExpired());
        assertTrue(userDetails.isEnabled());

        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
        assertNotNull(authorities);
        assertEquals(1, authorities.size());
        assertEquals("use", authorities.iterator().next().getAuthority());
    }
}
