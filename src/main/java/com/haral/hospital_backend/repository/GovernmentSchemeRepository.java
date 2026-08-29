package com.haral.hospital_backend.repository;

import com.haral.hospital_backend.entity.GovernmentScheme;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GovernmentSchemeRepository
        extends JpaRepository<GovernmentScheme, Long> {

    List<GovernmentScheme> findByActiveTrue();
}