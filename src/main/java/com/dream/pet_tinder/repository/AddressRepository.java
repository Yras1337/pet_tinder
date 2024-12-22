package com.dream.pet_tinder.repository;

import com.dream.pet_tinder.model.address.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {

}
