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
            @RequestBody Map<String, Object> request) {

        String patientName =
                request.get("patientName") != null
                        ? request.get("patientName").toString()
                        : null;

        String phone =
                request.get("phone") != null
                        ? request.get("phone").toString()
                        : null;

        Long draftId = null;

        if (request.get("draftId") != null) {
            try {
                draftId = Long.valueOf(
                        request.get("draftId").toString()
                );
            } catch (NumberFormatException ignored) {
                // Treat invalid draftId as null
            }
        }


        if (patientName == null
                || patientName.trim().isEmpty()
                || phone == null
                || phone.trim().isEmpty()) {

            return ResponseEntity.badRequest().build();
        }


        AppointmentDraft draft =
                appointmentDraftService.saveDraft(
                        draftId,
                        patientName.trim(),
                        phone.trim()
                );


        return ResponseEntity.ok(draft);
    }


    // =====================================================
    // GET ACTIVE DRAFTS
    // =====================================================

    @GetMapping("/active")
    public ResponseEntity<List<AppointmentDraft>> getActiveDrafts() {

        return ResponseEntity.ok(
                appointmentDraftService.getActiveDrafts()
        );
    }
    @PutMapping("/{id}/submitted")
    public ResponseEntity<?> markAsSubmitted(
            @PathVariable Long id) {

        try {

            appointmentDraftService.markAsSubmitted(id);

            return ResponseEntity.ok(
                    Map.of(
                            "success", true,
                            "message", "Draft marked as submitted"
                    )
            );

        } catch (RuntimeException e) {

            return ResponseEntity
                    .badRequest()
                    .body(
                            Map.of(
                                    "success", false,
                                    "message", e.getMessage()
                            )
                    );
        }
    }
    // =====================================================
    // GET ADMIN DRAFT HISTORY
    // Includes FILLING + ABANDONED
    // =====================================================

    @GetMapping("/admin")
    public ResponseEntity<List<AppointmentDraft>> getAdminDrafts() {

        return ResponseEntity.ok(
                appointmentDraftService.getAdminDrafts()
        );
    }
}