package com.dream.pet_tinder.model.profile;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ProfileTest {

    @Test
    void testProfileCreation() {
        Profile profile = new Profile(1L, null, null, null, "Test Description");

        assertNotNull(profile);
        assertEquals(1L, profile.getId());
        assertEquals("Test Description", profile.getDescription());
        assertNull(profile.getOwner());
        assertNull(profile.getMother());
        assertNull(profile.getFather());
    }

    @Test
    void testProfileSetters() {
        Profile profile = new Profile();
        profile.setId(2L);
        profile.setDescription("Another Description");

        assertEquals(2L, profile.getId());
        assertEquals("Another Description", profile.getDescription());
    }
}
