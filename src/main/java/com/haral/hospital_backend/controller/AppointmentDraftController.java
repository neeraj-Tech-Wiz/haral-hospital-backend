package com.haral.hospital_backend.controller;

import com.haral.hospital_backend.entity.AppointmentDraft;
import com.haral.hospital_backend.service.AppointmentDraftService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/appointment-drafts")
public class AppointmentDraftController {

    private final AppointmentDraftService appointmentDraftService;

    public AppointmentDraftController(
            AppointmentDraftService appointmentDraftService) {

        this.appointmentDraftService =
                appointmentDraftService;
    }


    // =====================================================
    // CREATE / UPDATE DRAFT
    // =====================================================

    @PostMapping
    public ResponseEntity<AppointmentDraft> saveDraft(
            @RequestBody Map<String, String> request) {

        String patientName = request.get("patientName");
        String phone = request.get("phone");

        if (patientName == null
                || patientName.trim().isEmpty()
                || phone == null
                || phone.trim().isEmpty()) {

            return ResponseEntity.badRequest().build();
        }

        AppointmentDraft draft =
                appointmentDraftService.saveDraft(
                        patientName.trim(),
                        phone.trim()
                );

        return ResponseEntity.ok(draft);
    }


    // =====================================================
    // ACTIVE DRAFTS
    // =====================================================

    @GetMapping("/active")
    public ResponseEntity<List<AppointmentDraft>> getActiveDrafts() {

        return ResponseEntity.ok(
                appointmentDraftService.getActiveDrafts()
        );
    }


    // =====================================================
    // ALL DRAFTS FOR ADMIN
    // =====================================================

    @GetMapping("/all")
    public ResponseEntity<List<AppointmentDraft>> getAllDrafts() {

        return ResponseEntity.ok(
                appointmentDraftService.getAllDraftsForAdmin()
        );
    }


    // =====================================================
    // ABANDONED DRAFTS
    // =====================================================

    @GetMapping("/abandoned")
    public ResponseEntity<List<AppointmentDraft>> getAbandonedDrafts() {

        return ResponseEntity.ok(
                appointmentDraftService.getAbandonedDrafts()
        );
    }


    // =====================================================
    // MARK AS SUBMITTED
    // =====================================================

    @PutMapping("/submitted/{phone}")
    public ResponseEntity<?> markAsSubmitted(
            @PathVariable String phone) {

        appointmentDraftService.markAsSubmitted(phone);

        return ResponseEntity.ok(
                Map.of(
                        "success", true,
                        "message", "Draft marked as submitted"
                )
        );
    }
    @PutMapping("/{id}")
    public ResponseEntity<AppointmentDraft> updateDraft(
            @PathVariable Long id,
            @RequestBody Map<String, String> request) {

        String patientName = request.get("patientName");
        String phone = request.get("phone");

        if (patientName == null
                || patientName.trim().isEmpty()
                || phone == null
                || phone.trim().isEmpty()) {

            return ResponseEntity.badRequest().build();
        }

        AppointmentDraft updated =
                appointmentDraftService.updateDraft(
                        id,
                        patientName.trim(),
                        phone.trim()
                );

        return ResponseEntity.ok(updated);
    }
}