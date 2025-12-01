package com.anna.power.desafio_java_backend.infrastructure.repository;

import com.anna.power.desafio_java_backend.infrastructure.entities.Brand;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BrandRepository extends JpaRepository<Brand, Integer> {

    Optional<Brand> findByFipeBrandId(Integer fipeBrandId);

}
