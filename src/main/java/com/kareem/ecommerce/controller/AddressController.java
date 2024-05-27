package com.kareem.ecommerce.controller;

import com.kareem.ecommerce.model.Address;
import com.kareem.ecommerce.model.User;
import com.kareem.ecommerce.service.AddressService;
import com.kareem.ecommerce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/addresses")
public class AddressController {

    private final AddressService addressService;
    private final UserService userService;

    @Autowired
    public AddressController(AddressService addressService, UserService userService) {
        this.addressService = addressService;
        this.userService = userService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<Address>> getAddressesForUser(@PathVariable Long userId) {
        List<Address> addresses = addressService.getAddressesForUser(userId);
        return new ResponseEntity<>(addresses, HttpStatus.OK);
    }

    @PostMapping("/{userId}")
    public ResponseEntity<Address> createAddressForUser(@PathVariable Long userId, @RequestBody Address address) {
        Optional<User> user = userService.getUserById(userId);
        if (user.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Address createdAddress = addressService.createAddressForUser(address, user.get());
        return new ResponseEntity<>(createdAddress, HttpStatus.CREATED);
    }
}
