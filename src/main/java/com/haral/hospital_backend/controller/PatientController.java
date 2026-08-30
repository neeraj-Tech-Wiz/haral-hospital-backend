package com.haral.hospital_backend.controller;

import com.haral.hospital_backend.entity.Patient;
import com.haral.hospital_backend.service.PatientService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/patients")
@CrossOrigin(origins = "http://localhost:5173")
public class PatientController {

    private final PatientService patientService;

    public PatientController(
            PatientService patientService) {

        this.patientService = patientService;
    }


    // =====================================================
    // ADMIN — GET ALL PATIENTS
    // =====================================================

    @GetMapping
    public ResponseEntity<List<Patient>> getAllPatients() {

        return ResponseEntity.ok(
                patientService.getAllPatients()
        );
    }


    // =====================================================
    // ADMIN — GET ACTIVE PATIENTS
    // =====================================================

    @GetMapping("/active")
    public ResponseEntity<List<Patient>> getActivePatients() {

        return ResponseEntity.ok(
                patientService.getActivePatients()
        );
    }

    // =====================================================
    // ADMIN — SEARCH PATIENTS
    // =====================================================

    @GetMapping("/search")
    public ResponseEntity<List<Patient>> searchPatients(
            @RequestParam String query) {

        return ResponseEntity.ok(
                patientService.searchPatients(
                        query
                )
        );
    }


    // =====================================================
    // ADMIN — GET PATIENT BY ID
    // =====================================================

    @GetMapping("/{id}")
    public ResponseEntity<Patient> getPatientById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                patientService.getPatientById(id)
        );
    }


    // =====================================================
    // ADMIN — GET PATIENT BY PATIENT CODE
    // =====================================================

    @GetMapping("/code/{patientCode}")
    public ResponseEntity<Patient> getPatientByCode(
            @PathVariable String patientCode) {

        return ResponseEntity.ok(
                patientService.getPatientByCode(
                        patientCode
                )
        );
    }


    // =====================================================
    // ADMIN — CREATE PATIENT
    // =====================================================

    @PostMapping
    public ResponseEntity<Patient> createPatient(
            @RequestBody Patient patient) {

        return ResponseEntity.ok(
                patientService.createPatient(
                        patient
                )
        );
    }


    // =====================================================
    // ADMIN — UPDATE PATIENT
    // =====================================================

    @PutMapping("/{id}")
    public ResponseEntity<Patient> updatePatient(
            @PathVariable Long id,
            @RequestBody Patient patient) {

        return ResponseEntity.ok(
                patientService.updatePatient(
                        id,
                        patient
                )
        );
    }


    // =====================================================
    // ADMIN — DEACTIVATE PATIENT
    // =====================================================

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<Patient> deactivatePatient(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                patientService.deactivatePatient(
                        id
                )
        );
    }


    // =====================================================
    // ADMIN — ACTIVATE PATIENT
    // =====================================================

    @PatchMapping("/{id}/activate")
    public ResponseEntity<Patient> activatePatient(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                patientService.activatePatient(
                        id
                )
        );
    }


    // =====================================================
    // ADMIN — DELETE PATIENT
    // =====================================================

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePatient(
            @PathVariable Long id) {

        try {

            patientService.deletePatient(id);

            return ResponseEntity.ok(
                    Map.of(
                            "success", true,
                            "message",
                            "Patient deleted successfully"
                    )
            );

        } catch (Exception e) {

            return ResponseEntity
                    .badRequest()
                    .body(
                            Map.of(
                                    "success", false,
                                    "message",
                                    e.getMessage()
                            )
                    );
        }
    }
    // =====================================================
// ADMIN — BULK IMPORT PATIENTS
// =====================================================

    @PostMapping("/bulk-import")
    public ResponseEntity<?> bulkImportPatients(
            @RequestParam("file")
            MultipartFile file) {

        try {

            return ResponseEntity.ok(
                    patientService.bulkImportPatients(
                            file
                    )
            );

        } catch (Exception e) {

            return ResponseEntity
                    .badRequest()
                    .body(
                            Map.of(
                                    "success",
                                    false,

                                    "message",
                                    e.getMessage()
                            )
                    );
        }
    }
}