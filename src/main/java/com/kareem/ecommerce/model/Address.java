package com.kareem.ecommerce.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@ToString
public class Address {

    @Getter
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Setter
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference
    private User user;

    @Getter
    @Setter
    @Column(name = "address_name")
    private String addressName;

    @Getter
    @Setter
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Getter
    @Setter
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Getter
    @Setter
    @Column(name = "street_address", nullable = false)
    private String streetAddress;

    @Getter
    @Setter
    @Column(name = "apartment_suite")
    private String apartmentSuite;

    @Getter
    @Setter
    @Column(name = "city", nullable = false)
    private String city;

    @Getter
    @Setter
    @Column(name = "state", nullable = false)
    private String state;

    @Getter
    @Setter
    @Column(name = "zip_code", nullable = false)
    private String zipCode;

    @Getter
    @Setter
    @Column(name = "country", nullable = false)
    private String country;

    @Setter
    @Column(name = "default_shipping_address")
    private boolean defaultShippingAddress;

    @Setter
    @Column(name = "default_billing_address")
    private boolean defaultBillingAddress;

    public boolean isDefaultShippingAddress() {
        return defaultShippingAddress;
    }

    public boolean isDefaultBillingAddress() {
        return defaultBillingAddress;
    }
}
