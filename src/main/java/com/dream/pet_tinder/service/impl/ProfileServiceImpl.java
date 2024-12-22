package com.dream.pet_tinder.service.impl;

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

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

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
                    .map(Characteristics::getValue).findFirst().orElseThrow(RuntimeException::new);

            List<Photo> photos = photoRepository.findAllByProfile(profile);
            byte[] imageData = photos.stream()
                    .filter(Photo::isMain)
                    .map(Photo::getImageData).findFirst().orElseThrow(RuntimeException::new);
            String imageDataAsBase64 = Base64.getEncoder().encodeToString(imageData);

            ProfileDto profileDto = new ProfileDto();
            profileDto.setName(name);
            profileDto.setId(profile.getId().toString());
            profileDto.setAlbumPhoto(imageDataAsBase64);
            profileDtos.add(profileDto);
        }

        return profileDtos;
    }

    @Override
    public Profile getUserPetsProfile(Long id) {
        return profileRepository.getById(id);
    }

    @Override
    public Profile updateUserPetsProfile(Profile profile, Long id) {
        Profile currentProfile = getUserPetsProfile(id);
        //currentProfile.setAddress(profile.getAddress());
        currentProfile.setDescription(profile.getDescription());

        Profile newFather = profileRepository.getById(profile.getFather().getId());
        Profile newMother = profileRepository.getById(profile.getMother().getId());

        currentProfile.setFather(newFather);
        currentProfile.setMother(newMother);

        return currentProfile;
    }

    @Override
    public void createNewProfile(ProfileDto newProfile) {
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

        for (String characteristic : newProfile.getCustom()) {
            saveCharacteristic(profile, characteristic, Characteristic.CUSTOM);
        }

        savePhoto(profile, newProfile.getMainPhoto(), true);
        for (byte[] photo : newProfile.getPhotos()) {
            savePhoto(profile, photo, false);
        }
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
