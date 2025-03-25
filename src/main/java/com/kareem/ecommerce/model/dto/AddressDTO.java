package com.kareem.ecommerce.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AddressDTO {

    private String addressName;

    private String apartmentSuite;

    @NotNull(message = "City cannot be null")
    private String city;

    private String country;

    private boolean defaultBillingAddress;

    private boolean defaultShippingAddress;

    private String firstName;

    private String lastName;

    private String streetAddress;

    private String state;

    private String zipCode;
}
