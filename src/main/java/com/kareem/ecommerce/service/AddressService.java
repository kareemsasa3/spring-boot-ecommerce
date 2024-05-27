package com.kareem.ecommerce.service;

import com.kareem.ecommerce.model.Address;
import com.kareem.ecommerce.model.User;
import com.kareem.ecommerce.repository.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressService {

    private final AddressRepository addressRepository;

    @Autowired
    public AddressService(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    public List<Address> getAddressesForUser(Long userId) {
        return addressRepository.findByUserId(userId);
    }

    public Address createAddressForUser(Address address, User user) {
        address.setUser(user);
        return addressRepository.save(address);
    }
}
