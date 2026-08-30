package com.haral.hospital_backend.repository;

import com.haral.hospital_backend.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PatientRepository
        extends JpaRepository<Patient, Long> {

    Optional<Patient> findByPatientCode(
            String patientCode
    );

    boolean existsByPatientCode(
            String patientCode
    );

    List<Patient> findAllByOrderByCreatedAtDesc();

    List<Patient> findByActiveTrueOrderByCreatedAtDesc();


    // =====================================================
    // SEARCH
    // Patient Code OR Name OR Mobile
    // =====================================================

    List<Patient> findByPatientCodeContainingIgnoreCaseOrNameContainingIgnoreCaseOrMobileContaining(
            String patientCode,
            String name,
            String mobile
    );
    boolean existsByMobile(String mobile);

    boolean existsByIdentityNumber(
            String identityNumber
    );
}