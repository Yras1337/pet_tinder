package com.dream.pet_tinder.service.impl;

import com.dream.pet_tinder.dto.ChatDto;
import com.dream.pet_tinder.dto.MessageDto;
import com.dream.pet_tinder.model.like.Like;
import com.dream.pet_tinder.model.message.Message;
import com.dream.pet_tinder.model.photo.Photo;
import com.dream.pet_tinder.model.profile.Profile;
import com.dream.pet_tinder.repository.LikeRepository;
import com.dream.pet_tinder.repository.MessageRepository;
import com.dream.pet_tinder.repository.PhotoRepository;
import com.dream.pet_tinder.repository.ProfileRepository;
import com.dream.pet_tinder.service.ChatService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Base64;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final ProfileRepository profileRepository;
    private final LikeRepository likeRepository;
    private final PhotoRepository photoRepository;
    private final MessageRepository messageRepository;

    @Override
    public List<ChatDto> getChats(Long id) {
        Profile profile = profileRepository.findProfileById(id);

        Set<Long> profileLikesIds = likeRepository.findAllByFirstProfile(profile).stream().map(like -> like.getSecondProfile().getId()).collect(Collectors.toSet());
        List<Like> incomeLikes = likeRepository.findAllBySecondProfile(profile);
        List<Profile> potentialChatsProfiles = incomeLikes.stream()
                .map(Like::getFirstProfile)
                .filter(firstProfile -> profileLikesIds.contains(firstProfile.getId()))
                .toList();

        List<Message> profileMessages = messageRepository.findAllByFirstProfile(profile);
        profileMessages.sort(Comparator.comparing(Message::getTime).reversed());

        List<ChatDto> chatDtoList = new ArrayList<>();
        for (Profile p: potentialChatsProfiles) {
            List<Photo> photos = photoRepository.findAllByProfile(p);
            byte[] imageData = photos.stream()
                    .filter(Photo::isMain)
                    .map(Photo::getImageData).findFirst().orElseThrow(RuntimeException::new);
            String imageDataAsBase64 = Base64.getEncoder().encodeToString(imageData);

            Message lastMessage = profileMessages.stream()
                    .filter(message -> message.getSecondProfile().getId().equals(p.getId()))
                    .findFirst()
                    .orElse(null);

            Long messageCount = profileMessages.stream()
                    .filter(message -> message.getSecondProfile().getId().equals(p.getId()))
                    .filter(message -> !message.getIsRead())
                    .count();

            ChatDto chatDto = new ChatDto();
            chatDto.setPhoto(imageDataAsBase64);
            if (Objects.nonNull(lastMessage)) {
                chatDto.setLastMessage(lastMessage.getText());
                chatDto.setTime(lastMessage.getTime());
            }
            chatDto.setMessageCount(messageCount);
            chatDto.setId(p.getId());
            chatDtoList.add(chatDto);
        }

        chatDtoList.sort(Comparator.comparing(ChatDto::getTime).reversed());
        return chatDtoList;
    }

    @Override
    public List<MessageDto> getChat(Long id, Long pId) {
        Profile firstProfile = profileRepository.findProfileById(id);
        List<Message> profileMessages = messageRepository.findAllByFirstProfile(firstProfile);
        profileMessages.sort(Comparator.comparing(Message::getTime).reversed());
        Profile secondProfile  = profileRepository.findProfileById(pId);
        profileMessages = profileMessages.stream()
                .filter(message -> message.getSecondProfile().getId().equals(secondProfile.getId()))
                .toList();

        List<MessageDto> messageDtos = new ArrayList<>();
        for (Message msg: profileMessages) {
            MessageDto messageDto = new MessageDto();
            messageDto.setMessage(msg.getText());
            messageDtos.add(messageDto);
            msg.setIsRead(true);
            messageRepository.save(msg);
        }

        return messageDtos;
    }

    @Override
    public void saveMessage(MessageDto message) {
        Profile firstProfile = profileRepository.findProfileById(message.getSender());
        Profile secondProfile = profileRepository.findProfileById(message.getRecipient());

        Message firstMessageEntity = new Message();
        firstMessageEntity.setIsRead(true);
        firstMessageEntity.setFirstProfile(firstProfile);
        firstMessageEntity.setSecondProfile(secondProfile);
        firstMessageEntity.setText(message.getMessage());
        firstMessageEntity.setTime(System.currentTimeMillis());

        Message secondMessageEntity = new Message();
        secondMessageEntity.setIsRead(false);
        secondMessageEntity.setFirstProfile(secondProfile);
        secondMessageEntity.setSecondProfile(firstProfile);
        secondMessageEntity.setText(message.getMessage());
        secondMessageEntity.setTime(System.currentTimeMillis());
        messageRepository.save(secondMessageEntity);
        messageRepository.save(firstMessageEntity);
    }
}
