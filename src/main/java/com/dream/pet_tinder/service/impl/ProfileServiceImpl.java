package com.dream.pet_tinder.service.impl;

import com.dream.pet_tinder.dto.PhotosDto;
import com.dream.pet_tinder.dto.ProfileDto;
import com.dream.pet_tinder.model.address.Address;
import com.dream.pet_tinder.model.characteristics.Characteristic;
import com.dream.pet_tinder.model.characteristics.Characteristics;
import com.dream.pet_tinder.model.photo.Photo;
import com.dream.pet_tinder.model.profile.Profile;
import com.dream.pet_tinder.model.user.User;
import com.dream.pet_tinder.repository.AddressRepository;
import com.dream.pet_tinder.repository.CharacteristicsRepository;
import com.dream.pet_tinder.repository.PhotoRepository;
import com.dream.pet_tinder.repository.ProfileRepository;
import com.dream.pet_tinder.security.AuthContextHandler;
import com.dream.pet_tinder.service.ProfileService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final ProfileRepository profileRepository;
    private final CharacteristicsRepository characteristicsRepository;
    private final PhotoRepository photoRepository;
    private final AuthContextHandler authContextHandler;
    private final AddressRepository addressRepository;

    @Override
    public List<ProfileDto> getUserPetsProfiles() {
        User user = authContextHandler.getLoggedInUser();
        List<Profile> profiles = profileRepository.findAllByOwner(user);
        List<ProfileDto> profileDtos = new ArrayList<>();

        for (Profile profile : profiles) {
            List<Characteristics> characteristics = characteristicsRepository.findAllByProfile(profile);
            String name = characteristics.stream()
                    .filter(x -> x.getCharacteristicName().equals(Characteristic.NAME))
                    .map(Characteristics::getValue)
                    .filter(value -> !Objects.isNull(value)).findFirst().orElseThrow(RuntimeException::new);

            List<Photo> photos = photoRepository.findAllByProfile(profile);
            byte[] imageData = photos.stream()
                    .filter(Photo::isMain)
                    .map(Photo::getImageData).findFirst().orElseThrow(RuntimeException::new);
            String imageDataAsBase64 = Base64.getEncoder().encodeToString(imageData);

            ProfileDto profileDto = new ProfileDto();
            profileDto.setName(name);
            profileDto.setId(profile.getId());
            profileDto.setAlbumPhoto(imageDataAsBase64);
            profileDtos.add(profileDto);
        }

        return profileDtos;
    }

    @Override
    public ProfileDto getUserPetsProfile(Long id) {
        ProfileDto profileDto = new ProfileDto();
        Profile profile = profileRepository.findProfileById(id);
        profileDto.setDescription(profile.getDescription());
        profileDto.setId(profile.getId());

        Address address = addressRepository.findByProfile(profile).stream().findFirst().orElseThrow(RuntimeException::new);

        List<Photo> photos = photoRepository.findAllByProfile(profile);
        byte[] imageData = photos.stream()
                .filter(Photo::isMain)
                .map(Photo::getImageData).findFirst().orElseThrow(RuntimeException::new);
        String imageDataAsBase64 = Base64.getEncoder().encodeToString(imageData);

        List<Characteristics> characteristicsList = characteristicsRepository.findAllByProfile(profile);

        profileDto.setAlbumPhoto(imageDataAsBase64);
        profileDto.setCountry(address.getCountry());
        profileDto.setCity(address.getCity());
        if (Objects.nonNull(characteristicsList)) {
            List<String> custom = new ArrayList<>();
            for (Characteristics characteristic : characteristicsList) {
                if (characteristic.getCharacteristicName().toString().equals(Characteristic.NAME.toString())) {
                    profileDto.setName(characteristic.getValue());
                } else if (characteristic.getCharacteristicName().toString().equals(Characteristic.TYPE.toString())) {
                    profileDto.setType(characteristic.getValue());
                } else {
                    custom.add(characteristic.getValue());
                }
            }
            String cus = String.join(", ", custom);
            profileDto.setOutCustom(cus);
        }

        return profileDto;
    }

    @Override
    @Transactional
    public void updateUserPetsProfile(ProfileDto newProfile, Long id) {
        Profile profile = profileRepository.findProfileById(id);
        profile.setDescription(newProfile.getDescription());
        profileRepository.save(profile);

        Address address = addressRepository.findByProfile(profile).stream().findFirst().orElseThrow(RuntimeException::new);
        address.setCountry(newProfile.getCountry());
        address.setCity(newProfile.getCity());
        addressRepository.save(address);

        characteristicsRepository.deleteAllByProfile(profile);
        saveCharacteristic(profile, newProfile.getName(), Characteristic.NAME);
        saveCharacteristic(profile, newProfile.getType(), Characteristic.TYPE);

        if (Objects.nonNull(newProfile.getCustom())) {
            for (String characteristic : newProfile.getCustom()) {
                saveCharacteristic(profile, characteristic, Characteristic.CUSTOM);
            }
        }
    }

    @Override
    public void createNewProfile(ProfileDto newProfile) throws IOException {
        User user = authContextHandler.getLoggedInUser();
        Profile profile = new Profile();
        profile.setOwner(user);
        profile.setDescription(newProfile.getDescription());
        profileRepository.save(profile);

        Address address = new Address();
        address.setProfile(profile);
        address.setCity(newProfile.getCity());
        address.setCountry(newProfile.getCountry());
        addressRepository.save(address);


        saveCharacteristic(profile, newProfile.getName(), Characteristic.NAME);
        saveCharacteristic(profile, newProfile.getType(), Characteristic.TYPE);

        if (Objects.nonNull(newProfile.getCustom())) {
            for (String characteristic : newProfile.getCustom()) {
                saveCharacteristic(profile, characteristic, Characteristic.CUSTOM);
            }
        }
        savePhoto(profile, newProfile.getMainPhoto().getBytes(), true);
        for (MultipartFile photo : newProfile.getPhotos()) {
            savePhoto(profile, photo.getBytes(), false);
        }
    }

    @Override
    public List<PhotosDto> getProfilePhotos(Long id) {
        Profile profile = profileRepository.findProfileById(id);
        List<Photo> photos = photoRepository.findAllByProfile(profile);
        List<PhotosDto> photosDtos = new ArrayList<>();
        for (Photo photo: photos) {
            if (photo.isMain()) {
                continue;
            }
            PhotosDto photosDto = new PhotosDto();
            photosDto.setPhoto(Base64.getEncoder().encodeToString(photo.getImageData()));
            photosDto.setPhotoId(photo.getId());
            photosDtos.add(photosDto);
        }

        return photosDtos;
    }

    private void saveCharacteristic(Profile profile, String newCharacteristic, Characteristic type) {
        Characteristics characteristics = new Characteristics();
        characteristics.setProfile(profile);
        characteristics.setCharacteristicName(type);
        characteristics.setValue(newCharacteristic);
        characteristicsRepository.save(characteristics);
    }

    private void savePhoto(Profile profile, byte[] newPhoto, boolean isMain) {
        Photo photo = new Photo();
        photo.setProfile(profile);
        photo.setImageData(newPhoto);
        photo.setMain(isMain);
        photoRepository.save(photo);
    }
}
