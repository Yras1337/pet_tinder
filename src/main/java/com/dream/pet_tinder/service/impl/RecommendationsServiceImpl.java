package com.dream.pet_tinder.service.impl;

import com.dream.pet_tinder.dto.ProfileDto;
import com.dream.pet_tinder.model.characteristics.Characteristic;
import com.dream.pet_tinder.model.characteristics.Characteristics;
import com.dream.pet_tinder.model.photo.Photo;
import com.dream.pet_tinder.model.profile.Profile;
import com.dream.pet_tinder.model.viewed_profile.ViewedProfile;
import com.dream.pet_tinder.repository.CharacteristicsRepository;
import com.dream.pet_tinder.repository.PhotoRepository;
import com.dream.pet_tinder.repository.ProfileRepository;
import com.dream.pet_tinder.repository.ViewedProfileRepository;
import com.dream.pet_tinder.service.RecommendationsService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

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
        ProfileDto recomendedProfile = null;
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

                recomendedProfile = profileDto;
            }
        }

        return recomendedProfile;
    }
}
