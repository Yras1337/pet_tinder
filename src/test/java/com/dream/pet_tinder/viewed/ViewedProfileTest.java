package com.dream.pet_tinder.model.viewed_profile;

import com.dream.pet_tinder.model.profile.Profile;
import com.dream.pet_tinder.model.viewed_profile.ViewedProfile;
import com.dream.pet_tinder.model.user.User;
import com.dream.pet_tinder.repository.ProfileRepository;
import com.dream.pet_tinder.repository.UserRepository;
import com.dream.pet_tinder.repository.ViewedProfileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
public class ViewedProfileTest {

    @Autowired
    private ViewedProfileRepository viewedProfileRepository;  // Репозиторий для работы с ViewedProfile
    @Autowired
    private ProfileRepository profileRepository;  // Репозиторий для работы с Profile
    @Autowired
    private UserRepository userRepository;  // Репозиторий для User

    private Profile profile1;
    private Profile profile2;
    private Profile profile3;

    @BeforeEach
    public void setUp() {
        // Создаем пользователей
        User user1 = new User();
        user1.setName("User 1");  // Убедись, что в User есть поле name или другие обязательные поля
        
        User user2 = new User();
        user2.setName("User 2");
        
        // Сохраняем пользователей
        user1 = userRepository.save(user1);
        user2 = userRepository.save(user2);

        // Создаем профили с привязкой к сохраненным пользователям
        profile1 = new Profile();
        profile1.setOwner(user1);
        profile1.setDescription("Profile 1 description");
        
        profile2 = new Profile();
        profile2.setOwner(user2);
        profile2.setDescription("Profile 2 description");

        profile3 = new Profile();
        profile3.setOwner(user1);
        profile3.setDescription("Profile 3 description");

        // Сохраняем профили в базе данных
        profile1 = profileRepository.save(profile1);
        profile2 = profileRepository.save(profile2);
        profile3 = profileRepository.save(profile3);
    }

    @Test
    public void testCreateViewedProfile() {
        // Создаем объект ViewedProfile и используем сеттеры для задания значений
        ViewedProfile viewedProfile = new ViewedProfile();
        viewedProfile.setFirstProfile(profile1);
        viewedProfile.setSecondProfile(profile2);

        // Сохраняем объект ViewedProfile в базе данных
        ViewedProfile savedViewedProfile = viewedProfileRepository.save(viewedProfile);

        // Проверяем, что объект был сохранен и имеет ID
        assertNotNull(savedViewedProfile.getId());
        assertNotNull(savedViewedProfile.getFirstProfile());
        assertNotNull(savedViewedProfile.getSecondProfile());
    }

    @Test
    public void testFindViewedProfileById() {
        // Сохраняем ViewedProfile
        ViewedProfile viewedProfile = new ViewedProfile();
        viewedProfile.setFirstProfile(profile1);
        viewedProfile.setSecondProfile(profile2);
        viewedProfile = viewedProfileRepository.save(viewedProfile);

        // Проверяем, что его можно найти по ID
        ViewedProfile foundProfile = viewedProfileRepository.findById(viewedProfile.getId()).orElse(null);

        assertNotNull(foundProfile);
        assertNotNull(foundProfile.getFirstProfile());
        assertNotNull(foundProfile.getSecondProfile());
    }

    @Test
    public void testFindAllByFirstProfile() {
        // Создаем несколько записей для разных профилей
        ViewedProfile viewedProfile1 = new ViewedProfile();
        viewedProfile1.setFirstProfile(profile1);
        viewedProfile1.setSecondProfile(profile2);
        
        ViewedProfile viewedProfile2 = new ViewedProfile();
        viewedProfile2.setFirstProfile(profile1);
        viewedProfile2.setSecondProfile(profile3);
        
        // Сохраняем их в базу данных
        viewedProfileRepository.save(viewedProfile1);
        viewedProfileRepository.save(viewedProfile2);

        // Проверяем, что метод findAllByFirstProfile возвращает правильные данные
        List<ViewedProfile> viewedProfiles = viewedProfileRepository.findAllByFirstProfile(profile1);
        assertNotNull(viewedProfiles);
        assertEquals(2, viewedProfiles.size());  // Должно вернуть два профиля, связанные с profile1
    }
}
