package com.kareem.ecommerce.service;

import com.kareem.ecommerce.model.Address;
import com.kareem.ecommerce.model.User;
import com.kareem.ecommerce.model.dto.AddressDTO;
import com.kareem.ecommerce.repository.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional(readOnly = true)
    public Address getDefaultShippingAddress(Long userId) {
        return addressRepository.findByUserIdAndDefaultShippingAddressTrue(userId);
    }

    @Transactional(readOnly = true)
    public Address getDefaultBillingAddress(Long userId) {
        return addressRepository.findByUserIdAndDefaultBillingAddressTrue(userId);
    }

    @Transactional
    public Address createAddressForUser(AddressDTO addressDTO, User user) {
        if (addressDTO.isDefaultBillingAddress()) {
            Address existingDefaultBilling = addressRepository.findByUserIdAndDefaultBillingAddressTrue(user.getId());
            if (existingDefaultBilling != null) {
                existingDefaultBilling.setDefaultBillingAddress(false);
                addressRepository.save(existingDefaultBilling);
            }
        }

        if (addressDTO.isDefaultShippingAddress()) {
            Address existingDefaultShipping = addressRepository.findByUserIdAndDefaultShippingAddressTrue(user.getId());
            if (existingDefaultShipping != null) {
                existingDefaultShipping.setDefaultBillingAddress(false);
                addressRepository.save(existingDefaultShipping);
            }
        }

        Address address = setAddressFromDTO(addressDTO, user);
        System.out.println(address);
        return addressRepository.save(address);
    }

    private static Address setAddressFromDTO(AddressDTO addressDTO, User user) {
        Address address = new Address();
        address.setAddressName(addressDTO.getAddressName());
        address.setFirstName(addressDTO.getFirstName());
        address.setLastName(addressDTO.getLastName());
        address.setStreetAddress(addressDTO.getStreetAddress());
        address.setApartmentSuite(addressDTO.getApartmentSuite());
        address.setCity(addressDTO.getCity());
        address.setState(addressDTO.getState());
        address.setZipCode(addressDTO.getZipCode());
        address.setCountry(addressDTO.getCountry());
        address.setDefaultBillingAddress(addressDTO.isDefaultBillingAddress());
        address.setDefaultShippingAddress(addressDTO.isDefaultShippingAddress());
        address.setUser(user);
        return address;
    }
}
