package com.haral.hospital_backend.repository;

import com.haral.hospital_backend.entity.Speciality;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpecialityRepository
        extends JpaRepository<Speciality, Long> {

    List<Speciality> findByActiveTrueOrderByDisplayOrderAsc();

    List<Speciality> findAllByOrderByDisplayOrderAsc();
}