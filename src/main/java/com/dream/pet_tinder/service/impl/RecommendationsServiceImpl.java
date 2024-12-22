package com.dream.pet_tinder.service.impl;

import com.dream.pet_tinder.dto.ProfileDto;
import com.dream.pet_tinder.model.characteristics.Characteristic;
import com.dream.pet_tinder.model.characteristics.Characteristics;
import com.dream.pet_tinder.model.like.Like;
import com.dream.pet_tinder.model.message.Message;
import com.dream.pet_tinder.model.photo.Photo;
import com.dream.pet_tinder.model.profile.Profile;
import com.dream.pet_tinder.model.viewed_profile.ViewedProfile;
import com.dream.pet_tinder.repository.CharacteristicsRepository;
import com.dream.pet_tinder.repository.LikeRepository;
import com.dream.pet_tinder.repository.MessageRepository;
import com.dream.pet_tinder.repository.PhotoRepository;
import com.dream.pet_tinder.repository.ProfileRepository;
import com.dream.pet_tinder.repository.ViewedProfileRepository;
import com.dream.pet_tinder.service.RecommendationsService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class RecommendationsServiceImpl implements RecommendationsService {

    private final ViewedProfileRepository viewedProfileRepository;
    private final CharacteristicsRepository characteristicsRepository;
    private final ProfileRepository profileRepository;
    private final PhotoRepository photoRepository;
    private final LikeRepository likeRepository;
    private final MessageRepository messageRepository;

    @Override
    public ProfileDto getNextRecommendation(Long id) {
        Profile profile = profileRepository.findProfileById(id);
        List<Profile> potentialRecommendations = profileRepository.findAll();

        List<ViewedProfile> viewedProfiles = viewedProfileRepository.findAllByFirstProfile(profile);
        List<Long> viewedProfilesIds = viewedProfiles.stream().map(viewedProfile -> viewedProfile.getFirstProfile().getId()).toList();

        List<Characteristics> characteristics = characteristicsRepository.findAllByProfile(profile);
        String petType = characteristics.stream()
                .filter(x -> x.getCharacteristicName().equals(Characteristic.TYPE))
                .map(Characteristics::getValue)
                .filter(value -> !Objects.isNull(value)).findFirst().orElseThrow(RuntimeException::new);
        Set<String> tags = characteristics.stream()
                .filter(x -> x.getCharacteristicName().equals(Characteristic.CUSTOM))
                .map(Characteristics::getValue)
                .filter(value -> !Objects.isNull(value)).collect(Collectors.toSet());

        Long maximum = 0L;
        ProfileDto recommendedProfile = null;
        for (Profile p : potentialRecommendations) {
            if (p.getId().equals(profile.getId()) || viewedProfilesIds.contains(profile.getId()) || profile.getOwner().getId().equals(p.getOwner().getId())) {
                continue;
            }

            List<Characteristics> pCharacteristics = characteristicsRepository.findAllByProfile(p);
            String pType = pCharacteristics.stream()
                    .filter(x -> x.getCharacteristicName().equals(Characteristic.TYPE))
                    .map(Characteristics::getValue)
                    .filter(value -> !Objects.isNull(value)).findFirst().orElseThrow(RuntimeException::new);

            if (!petType.equals(pType)) {
                continue;
            }

            Long cnt = pCharacteristics.stream()
                    .filter(x -> x.getCharacteristicName().equals(Characteristic.CUSTOM))
                    .map(Characteristics::getValue)
                    .filter(value -> !Objects.isNull(value))
                    .filter(tags::contains)
                    .count();
            if (cnt > maximum) {
                maximum = cnt;

                List<Photo> photos = photoRepository.findAllByProfile(p);
                byte[] imageData = photos.stream()
                        .filter(Photo::isMain)
                        .map(Photo::getImageData).findFirst().orElseThrow(RuntimeException::new);
                String imageDataAsBase64 = Base64.getEncoder().encodeToString(imageData);
                String name = pCharacteristics.stream()
                        .filter(x -> x.getCharacteristicName().equals(Characteristic.NAME))
                        .map(Characteristics::getValue)
                        .filter(value -> !Objects.isNull(value)).findFirst().orElseThrow(RuntimeException::new);

                ProfileDto profileDto = new ProfileDto();
                profileDto.setName(name);
                profileDto.setId(p.getId());
                profileDto.setDescription(p.getDescription());
                profileDto.setAlbumPhoto(imageDataAsBase64);

                recommendedProfile = profileDto;
            }
        }
        return recommendedProfile;
    }

    @Override
    public void processMatch(Long id, Long pId, String flag) {
        Profile firstProfile = profileRepository.findProfileById(id);
        Profile secondProfile = profileRepository.findProfileById(pId);

        if (flag.equals("match_false")) {
            ViewedProfile firstViewedProfile = new ViewedProfile();
            firstViewedProfile.setFirstProfile(firstProfile);
            firstViewedProfile.setSecondProfile(secondProfile);
            viewedProfileRepository.save(firstViewedProfile);

            ViewedProfile secondViewedProfile = new ViewedProfile();
            secondViewedProfile.setFirstProfile(secondProfile);
            secondViewedProfile.setSecondProfile(firstProfile);
            viewedProfileRepository.save(secondViewedProfile);
            return;
        }

        Like firstLike = new Like();
        firstLike.setFirstProfile(firstProfile);
        firstLike.setSecondProfile(secondProfile);
        likeRepository.save(firstLike);
        Like secondLike = likeRepository.findAllByFirstProfile(secondProfile).stream()
                .filter(like -> like.getSecondProfile().getId().equals(firstProfile.getId()))
                .findFirst()
                .orElse(null);

        if (Objects.nonNull(secondLike)) {
            Message firstMessage = new Message();
            firstMessage.setFirstProfile(firstProfile);
            firstMessage.setSecondProfile(secondProfile);
            firstMessage.setText("Match!");
            firstMessage.setIsRead(false);
            firstMessage.setTime(System.currentTimeMillis());
            firstMessage.setSender(firstProfile);

            Message secondMessage = new Message();
            secondMessage.setFirstProfile(secondProfile);
            secondMessage.setSecondProfile(firstProfile);
            secondMessage.setText("Match!");
            secondMessage.setIsRead(false);
            secondMessage.setTime(System.currentTimeMillis());
            secondMessage.setSender(firstProfile);

            messageRepository.save(firstMessage);
            messageRepository.save(secondMessage);
        }

        ViewedProfile firstViewedProfile = new ViewedProfile();
        firstViewedProfile.setFirstProfile(firstProfile);
        firstViewedProfile.setSecondProfile(secondProfile);
        viewedProfileRepository.save(firstViewedProfile);
    }

    @Override
    public ProfileDto getPotentialRecommendation(Long id) {
        Profile profile = profileRepository.findProfileById(id);

        List<ViewedProfile> viewedProfiles = viewedProfileRepository.findAllByFirstProfile(profile);
        Set<Long> viewedProfilesSet = viewedProfiles.stream().map(viewedProfile -> viewedProfile.getSecondProfile().getId()).collect(Collectors.toSet());

        List<Like> incomeLikes = likeRepository.findAllBySecondProfile(profile);
        Profile recommendation = incomeLikes.stream()
                .map(Like::getFirstProfile)
                .filter(rec -> !viewedProfilesSet.contains(rec.getId()))
                .findAny().orElse(null);
        ProfileDto recommendedProfile = null;

        if (Objects.nonNull(recommendation)) {
            List<Photo> photos = photoRepository.findAllByProfile(recommendation);
            List<Characteristics> characteristics = characteristicsRepository.findAllByProfile(recommendation);

            byte[] imageData = photos.stream()
                    .filter(Photo::isMain)
                    .map(Photo::getImageData).findFirst().orElseThrow(RuntimeException::new);
            String imageDataAsBase64 = Base64.getEncoder().encodeToString(imageData);
            String name = characteristics.stream()
                    .filter(x -> x.getCharacteristicName().equals(Characteristic.NAME))
                    .map(Characteristics::getValue)
                    .filter(value -> !Objects.isNull(value)).findFirst().orElseThrow(RuntimeException::new);

            ProfileDto profileDto = new ProfileDto();
            profileDto.setName(name);
            profileDto.setId(recommendation.getId());
            profileDto.setDescription(recommendation.getDescription());
            profileDto.setAlbumPhoto(imageDataAsBase64);
            recommendedProfile = profileDto;
        }

        return recommendedProfile;
    }
}
