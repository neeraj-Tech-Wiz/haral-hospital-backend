package com.haral.hospital_backend.service;

import com.haral.hospital_backend.entity.Patient;
import com.haral.hospital_backend.entity.PatientVisit;
import com.haral.hospital_backend.repository.PatientRepository;
import com.haral.hospital_backend.repository.PatientVisitRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientVisitService {

    private final PatientVisitRepository
            patientVisitRepository;

    private final PatientRepository
            patientRepository;


    public PatientVisitService(
            PatientVisitRepository patientVisitRepository,
            PatientRepository patientRepository) {

        this.patientVisitRepository =
                patientVisitRepository;

        this.patientRepository =
                patientRepository;
    }


    // =====================================================
    // CREATE VISIT
    // =====================================================

    public PatientVisit createVisit(
            Long patientId,
            PatientVisit visit) {

        Patient patient =
                patientRepository
                        .findById(patientId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Patient not found with id: "
                                                + patientId
                                )
                        );

        visit.setPatient(patient);

        return patientVisitRepository.save(
                visit
        );
    }


    // =====================================================
    // GET PATIENT VISITS
    // =====================================================

    public List<PatientVisit> getPatientVisits(
            Long patientId) {

        if (!patientRepository.existsById(
                patientId)) {

            throw new RuntimeException(
                    "Patient not found with id: "
                            + patientId
            );
        }

        return patientVisitRepository
                .findByPatientIdOrderByVisitDateDesc(
                        patientId
                );
    }


    // =====================================================
    // GET VISIT BY ID
    // =====================================================

    public PatientVisit getVisitById(
            Long visitId) {

        return patientVisitRepository
                .findById(visitId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Patient visit not found with id: "
                                        + visitId
                        )
                );
    }


    // =====================================================
    // UPDATE VISIT
    // =====================================================

    public PatientVisit updateVisit(
            Long visitId,
            PatientVisit visit) {

        PatientVisit existing =
                getVisitById(visitId);


        existing.setVisitDate(
                visit.getVisitDate()
        );

        existing.setDepartment(
                visit.getDepartment()
        );

        existing.setDoctor(
                visit.getDoctor()
        );

        existing.setReason(
                visit.getReason()
        );

        existing.setDiagnosis(
                visit.getDiagnosis()
        );

        existing.setTreatment(
                visit.getTreatment()
        );

        existing.setPrescription(
                visit.getPrescription()
        );

        existing.setNotes(
                visit.getNotes()
        );

        // Patient relationship is intentionally
        // NOT changed during update.

        return patientVisitRepository.save(
                existing
        );
    }


    // =====================================================
    // DELETE VISIT
    // =====================================================

    public void deleteVisit(
            Long visitId) {

        if (!patientVisitRepository
                .existsById(visitId)) {

            throw new RuntimeException(
                    "Patient visit not found with id: "
                            + visitId
            );
        }

        patientVisitRepository.deleteById(
                visitId
        );
    }
}