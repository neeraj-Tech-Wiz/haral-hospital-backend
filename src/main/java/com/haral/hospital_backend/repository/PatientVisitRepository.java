package com.haral.hospital_backend.repository;

import com.haral.hospital_backend.entity.Patient;
import com.haral.hospital_backend.entity.PatientVisit;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PatientVisitRepository
        extends JpaRepository<PatientVisit, Long> {

    List<PatientVisit>
    findByPatientOrderByVisitDateDesc(
            Patient patient
    );

    List<PatientVisit>
    findByPatientIdOrderByVisitDateDesc(
            Long patientId
    );
}