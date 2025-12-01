package com.anna.power.desafio_java_backend.infrastructure.repository;

import com.anna.power.desafio_java_backend.infrastructure.entities.Model;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ModelRepository extends JpaRepository<Model, Integer> {

    Optional<Model> findByFipeModelId(Integer fipeModelId);

}
