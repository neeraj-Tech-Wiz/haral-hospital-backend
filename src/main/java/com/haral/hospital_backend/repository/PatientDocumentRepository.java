package com.haral.hospital_backend.repository;

import com.haral.hospital_backend.entity.PatientDocument;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PatientDocumentRepository
        extends JpaRepository<PatientDocument, Long> {

    List<PatientDocument>
    findByPatientIdOrderByUploadedAtDesc(
            Long patientId
    );
}