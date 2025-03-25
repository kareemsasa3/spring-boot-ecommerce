package com.kareem.ecommerce.repository;

import com.kareem.ecommerce.model.Fandom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FandomRepository extends JpaRepository<Fandom, Long> {
}
