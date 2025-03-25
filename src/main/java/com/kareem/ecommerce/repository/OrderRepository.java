package com.kareem.ecommerce.repository;

import com.kareem.ecommerce.model.CustomerOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<CustomerOrder, Long> {
}
