package com.kareem.ecommerce.repository;

import com.kareem.ecommerce.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
    List<Address> findByUserId(Long userId);
    Address findByUserIdAndDefaultShippingAddressTrue(Long userId);
    Address findByUserIdAndDefaultBillingAddressTrue(Long userId);
}
