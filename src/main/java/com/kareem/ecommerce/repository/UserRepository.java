package com.kareem.ecommerce.repository;

import com.kareem.ecommerce.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByNormalizedUsername(String normalizedUsername);
    User findByEmail(String email);
}
