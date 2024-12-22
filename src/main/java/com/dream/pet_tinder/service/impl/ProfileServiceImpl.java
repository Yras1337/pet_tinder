package com.dream.pet_tinder.service.impl;

import com.dream.pet_tinder.dto.ProfileDto;
import com.dream.pet_tinder.model.address.Address;
import com.dream.pet_tinder.model.characteristics.Characteristic;
import com.dream.pet_tinder.model.characteristics.Characteristics;
import com.dream.pet_tinder.model.photo.Photo;
import com.dream.pet_tinder.model.profile.Profile;
import com.dream.pet_tinder.model.user.User;
import com.dream.pet_tinder.repository.CharacteristicsRepository;
import com.dream.pet_tinder.repository.PhotoRepository;
import com.dream.pet_tinder.repository.ProfileRepository;
import com.dream.pet_tinder.security.AuthContextHandler;
import com.dream.pet_tinder.service.ProfileService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final ProfileRepository profileRepository;
    private final CharacteristicsRepository characteristicsRepository;
    private final PhotoRepository photoRepository;
    private final AuthContextHandler authContextHandler;

    @Override
    public List<Profile> getUserPetsProfiles() {
        User user = authContextHandler.getLoggedInUser();

        return profileRepository.findAllByOwner(user);
    }

    @Override
    public Profile getUserPetsProfile(Long id) {
        return profileRepository.getById(id);
    }

    @Override
    public Profile updateUserPetsProfile(Profile profile, Long id) {
        Profile currentProfile = getUserPetsProfile(id);
        currentProfile.setAddress(profile.getAddress());
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
        Address address = new Address();
        address.setProfile(profile);
        address.setCity(newProfile.getCity());
        address.setCountry(newProfile.getCountry());
        profile.setAddress(address);
        profile.setOwner(user);
        profile.setDescription(newProfile.getDescription());
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
