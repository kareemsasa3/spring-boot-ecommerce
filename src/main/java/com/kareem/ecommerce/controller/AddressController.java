package com.kareem.ecommerce.controller;

import com.kareem.ecommerce.model.Address;
import com.kareem.ecommerce.model.User;
import com.kareem.ecommerce.model.dto.AddressDTO;
import com.kareem.ecommerce.service.AddressService;
import com.kareem.ecommerce.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/addresses")
@Validated
@CrossOrigin(origins = { "https://ecommerce-template.up.railway.app", "http://localhost:5173" })
public class AddressController {

    private final AddressService addressService;
    private final UserService userService;

    public AddressController(AddressService addressService, UserService userService) {
        this.addressService = addressService;
        this.userService = userService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<Address>> getAddressesForUser(@PathVariable Long userId) {
        List<Address> addresses = addressService.getAddressesForUser(userId);
        return new ResponseEntity<>(addresses, HttpStatus.OK);
    }

    @GetMapping("/default-shipping/{userId}")
    public ResponseEntity<Address> getDefaultShippingAddress(@PathVariable Long userId) {
        Address defaultShippingAddress = addressService.getDefaultShippingAddress(userId);
        if (defaultShippingAddress != null) {
            return ResponseEntity.ok(defaultShippingAddress);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/default-billing/{userId}")
    public ResponseEntity<Address> getDefaultBillingAddress(@PathVariable Long userId) {
        Address defaultBillingAddress = addressService.getDefaultBillingAddress(userId);
        if (defaultBillingAddress != null) {
            return ResponseEntity.ok(defaultBillingAddress);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{userId}")
    public ResponseEntity<Address> createAddressForUser(@PathVariable Long userId,
            @Valid @RequestBody AddressDTO addressDTO) {
        System.out.println(addressDTO);
        Optional<User> user = userService.getUserById(userId);
        if (user.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Address createdAddress = addressService.createAddressForUser(addressDTO, user.get());
        return new ResponseEntity<>(createdAddress, HttpStatus.CREATED);
    }
}
