package com.dream.pet_tinder.service.impl;

import com.dream.pet_tinder.dto.ChatDto;
import com.dream.pet_tinder.dto.MessageDto;
import com.dream.pet_tinder.model.characteristics.Characteristics;
import com.dream.pet_tinder.model.like.Like;
import com.dream.pet_tinder.model.message.Message;
import com.dream.pet_tinder.model.photo.Photo;
import com.dream.pet_tinder.model.profile.Profile;
import com.dream.pet_tinder.repository.CharacteristicsRepository;
import com.dream.pet_tinder.repository.LikeRepository;
import com.dream.pet_tinder.repository.MessageRepository;
import com.dream.pet_tinder.repository.PhotoRepository;
import com.dream.pet_tinder.repository.ProfileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;
import java.util.stream.Collectors;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ChatServiceImplTest {

    @InjectMocks
    private ChatServiceImpl chatService;

    @Mock
    private ProfileRepository profileRepository;

    @Mock
    private LikeRepository likeRepository;

    @Mock
    private PhotoRepository photoRepository;

    @Mock
    private MessageRepository messageRepository;

    @Mock
    private CharacteristicsRepository characteristicsRepository;

    private Profile profile;
    private Profile otherProfile;
    private Like like;
    private Message message;
    private Photo photo;
    private Characteristics characteristics;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        
        profile = new Profile();
        profile.setId(1L);
        otherProfile = new Profile();
        otherProfile.setId(2L);
        
        like = new Like();
        like.setFirstProfile(profile);
        like.setSecondProfile(otherProfile);

        message = new Message();
        message.setFirstProfile(profile);
        message.setSecondProfile(otherProfile);
        message.setText("Hello");
        message.setTime(System.currentTimeMillis());
        message.setIsRead(false);
        
        photo = new Photo();
        photo.setProfile(otherProfile);
        photo.setMain(true);
        photo.setImageData(new byte[]{1, 2, 3});
        
        characteristics = new Characteristics();
        characteristics.setProfile(profile);
        characteristics.setCharacteristicName(com.dream.pet_tinder.model.characteristics.Characteristic.NAME);
        characteristics.setValue("John Doe");
    }

    @Test
    public void testGetChats() {
        // Настроим моки
        when(profileRepository.findProfileById(1L)).thenReturn(profile);
        when(likeRepository.findAllByFirstProfile(profile)).thenReturn(Collections.singletonList(like));
        when(likeRepository.findAllBySecondProfile(profile)).thenReturn(Collections.singletonList(like));
        when(photoRepository.findAllByProfile(any(Profile.class))).thenReturn(Collections.singletonList(photo));
        when(messageRepository.findAllByFirstProfile(profile)).thenReturn(Collections.singletonList(message));

        // Выполним тест
        List<ChatDto> chats = chatService.getChats(1L);

        // Проверим результаты
        assertNotNull(chats);
    }

    @Test
    public void testGetChat() {
        // Настроим моки
        when(profileRepository.findProfileById(1L)).thenReturn(profile);
        when(profileRepository.findProfileById(2L)).thenReturn(otherProfile);
        when(messageRepository.findAllByFirstProfile(profile)).thenReturn(Collections.singletonList(message));
        when(characteristicsRepository.findAllByProfile(any(Profile.class)))
                .thenReturn(Collections.singletonList(characteristics));

        // Выполним тест
        List<MessageDto> messageDtos = chatService.getChat(1L, 2L);

        // Проверим результаты
        assertNotNull(messageDtos);
    }

    @Test
    public void testSaveMessage() {
        // Настроим моки
        MessageDto messageDto = new MessageDto();
        messageDto.setMessage("New Message");
        messageDto.setSender(1L);
        messageDto.setRecipient(2L);

        when(profileRepository.findProfileById(1L)).thenReturn(profile);
        when(profileRepository.findProfileById(2L)).thenReturn(otherProfile);

        chatService.saveMessage(messageDto);
    }
}
