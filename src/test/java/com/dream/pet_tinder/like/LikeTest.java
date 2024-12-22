package com.dream.pet_tinder.like;

import com.dream.pet_tinder.model.like.Like;
import com.dream.pet_tinder.model.profile.Profile;
import com.dream.pet_tinder.model.user.User;
import com.dream.pet_tinder.repository.LikeRepository;
import com.dream.pet_tinder.repository.ProfileRepository;
import com.dream.pet_tinder.repository.UserRepository;
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
public class LikeTest {

    @Autowired
    private LikeRepository likeRepository;  // Репозиторий для Like
    @Autowired
    private ProfileRepository profileRepository;  // Репозиторий для Profile
    @Autowired
    private UserRepository userRepository;  // Репозиторий для User

    private Profile profile1;
    private Profile profile2;

    @BeforeEach
    public void setUp() {
        // Создаем пользователей
        User user1 = new User();
        user1.setName("User 1");
        
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

        // Сохраняем профили в базе данных
        profile1 = profileRepository.save(profile1);
        profile2 = profileRepository.save(profile2);
    }

    @Test
    public void testCreateLike() {
        // Создаем объект Like и задаем связанные профили
        Like like = new Like();
        like.setFirstProfile(profile1);
        like.setSecondProfile(profile2);

        // Сохраняем объект Like в базе данных
        Like savedLike = likeRepository.save(like);

        // Проверяем, что объект был сохранен и имеет ID
        assertNotNull(savedLike.getId());
        assertNotNull(savedLike.getFirstProfile());
        assertNotNull(savedLike.getSecondProfile());
    }

    @Test
    public void testFindLikeById() {
        // Создаем и сохраняем Like
        Like like = new Like();
        like.setFirstProfile(profile1);
        like.setSecondProfile(profile2);
        like = likeRepository.save(like);

        // Проверяем, что его можно найти по ID
        Like foundLike = likeRepository.findById(like.getId()).orElse(null);

        assertNotNull(foundLike);
        assertNotNull(foundLike.getFirstProfile());
        assertNotNull(foundLike.getSecondProfile());
    }

    @Test
    public void testFindLikesByFirstProfile() {
        // Создаем несколько записей для разных профилей
        Like like1 = new Like();
        like1.setFirstProfile(profile1);
        like1.setSecondProfile(profile2);
        
        Like like2 = new Like();
        like2.setFirstProfile(profile1);
        like2.setSecondProfile(profile2);
        
        // Сохраняем их в базу данных
        likeRepository.save(like1);
        likeRepository.save(like2);

        List<Like> likes = likeRepository.findAllByFirstProfile(profile1);
        assertNotNull(likes);
        assertEquals(2, likes.size());  // Должно вернуть два лайка, связанные с profile1
    }
}
