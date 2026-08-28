package com.haral.hospital_backend.repository;

import com.haral.hospital_backend.entity.Facility;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FacilityRepository extends JpaRepository<Facility, Long> {

    List<Facility> findByActiveTrue();

}