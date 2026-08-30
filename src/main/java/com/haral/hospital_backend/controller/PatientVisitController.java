package com.haral.hospital_backend.controller;

import com.haral.hospital_backend.entity.PatientVisit;
import com.haral.hospital_backend.service.PatientVisitService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/patients")
@CrossOrigin(origins = "http://localhost:5173")
public class PatientVisitController {

    private final PatientVisitService
            patientVisitService;


    public PatientVisitController(
            PatientVisitService patientVisitService) {

        this.patientVisitService =
                patientVisitService;
    }


    // =====================================================
    // GET PATIENT VISITS
    // =====================================================

    @GetMapping("/{patientId}/visits")
    public ResponseEntity<List<PatientVisit>>
    getPatientVisits(
            @PathVariable Long patientId) {

        return ResponseEntity.ok(
                patientVisitService
                        .getPatientVisits(
                                patientId
                        )
        );
    }


    // =====================================================
    // CREATE PATIENT VISIT
    // =====================================================

    @PostMapping("/{patientId}/visits")
    public ResponseEntity<PatientVisit>
    createVisit(
            @PathVariable Long patientId,
            @RequestBody PatientVisit visit) {

        return ResponseEntity.ok(
                patientVisitService
                        .createVisit(
                                patientId,
                                visit
                        )
        );
    }


    // =====================================================
    // GET SINGLE VISIT
    // =====================================================

    @GetMapping("/visits/{visitId}")
    public ResponseEntity<PatientVisit>
    getVisit(
            @PathVariable Long visitId) {

        return ResponseEntity.ok(
                patientVisitService
                        .getVisitById(
                                visitId
                        )
        );
    }


    // =====================================================
    // UPDATE VISIT
    // =====================================================

    @PutMapping("/visits/{visitId}")
    public ResponseEntity<PatientVisit>
    updateVisit(
            @PathVariable Long visitId,
            @RequestBody PatientVisit visit) {

        return ResponseEntity.ok(
                patientVisitService
                        .updateVisit(
                                visitId,
                                visit
                        )
        );
    }


    // =====================================================
    // DELETE VISIT
    // =====================================================

    @DeleteMapping("/visits/{visitId}")
    public ResponseEntity<?> deleteVisit(
            @PathVariable Long visitId) {

        try {

            patientVisitService.deleteVisit(
                    visitId
            );

            return ResponseEntity.ok(
                    Map.of(
                            "success", true,
                            "message",
                            "Patient visit deleted successfully"
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
}