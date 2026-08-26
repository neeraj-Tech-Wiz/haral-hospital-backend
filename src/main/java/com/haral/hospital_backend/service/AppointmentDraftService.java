package com.haral.hospital_backend.service;

import com.haral.hospital_backend.entity.AppointmentDraft;
import com.haral.hospital_backend.repository.AppointmentDraftRepository;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AppointmentDraftService {

    private final AppointmentDraftRepository appointmentDraftRepository;

    private static final int ABANDON_TIMEOUT_MINUTES = 10;

    public AppointmentDraftService(
            AppointmentDraftRepository appointmentDraftRepository) {

        this.appointmentDraftRepository =
                appointmentDraftRepository;
    }


    // =====================================================
    // CREATE / UPDATE DRAFT
    // =====================================================

    public AppointmentDraft saveDraft(
            String patientName,
            String phone) {

        LocalDateTime now = LocalDateTime.now();

        // Clean old drafts first
        markInactiveDraftsAsAbandoned();

        AppointmentDraft draft =
                appointmentDraftRepository
                        .findFirstByPhoneAndStatus(
                                phone,
                                "FILLING"
                        )
                        .orElse(null);


        // Existing active draft
        if (draft != null) {

            draft.setPatientName(patientName);
            draft.setLastActiveAt(now);

            return appointmentDraftRepository.save(draft);
        }


        // New draft
        draft = new AppointmentDraft();

        draft.setPatientName(patientName);
        draft.setPhone(phone);
        draft.setStatus("FILLING");
        draft.setCreatedAt(now);
        draft.setLastActiveAt(now);

        return appointmentDraftRepository.save(draft);
    }


    // =====================================================
    // CURRENTLY ACTIVE
    // =====================================================

    public List<AppointmentDraft> getActiveDrafts() {

        markInactiveDraftsAsAbandoned();

        return appointmentDraftRepository
                .findByStatusOrderByLastActiveAtDesc(
                        "FILLING"
                );
    }


    // =====================================================
    // ALL DRAFTS
    // =====================================================

    public List<AppointmentDraft> getAllDraftsForAdmin() {

        // Important:
        // First update stale FILLING records.

        markInactiveDraftsAsAbandoned();

        return appointmentDraftRepository
                .findAllByOrderByLastActiveAtDesc();
    }


    // =====================================================
    // ABANDONED
    // =====================================================

    public List<AppointmentDraft> getAbandonedDrafts() {

        markInactiveDraftsAsAbandoned();

        return appointmentDraftRepository
                .findByStatusOrderByLastActiveAtDesc(
                        "ABANDONED"
                );
    }


    // =====================================================
    // MARK OLD FILLING AS ABANDONED
    // =====================================================

    public void markInactiveDraftsAsAbandoned() {

        LocalDateTime cutoff =
                LocalDateTime.now()
                        .minusMinutes(
                                ABANDON_TIMEOUT_MINUTES
                        );

        List<AppointmentDraft> staleDrafts =
                appointmentDraftRepository
                        .findByStatusAndLastActiveAtBefore(
                                "FILLING",
                                cutoff
                        );


        for (AppointmentDraft draft : staleDrafts) {

            draft.setStatus("ABANDONED");

            appointmentDraftRepository.save(draft);
        }
    }


    // =====================================================
    // MARK AS SUBMITTED
    // =====================================================

    public void markAsSubmitted(String phone) {

        appointmentDraftRepository
                .findFirstByPhoneAndStatus(
                        phone,
                        "FILLING"
                )
                .ifPresent(draft -> {

                    draft.setStatus("SUBMITTED");

                    draft.setLastActiveAt(
                            LocalDateTime.now()
                    );

                    appointmentDraftRepository.save(draft);
                });
    }
    public AppointmentDraft updateDraft(
            Long id,
            String patientName,
            String phone) {

        AppointmentDraft draft =
                appointmentDraftRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Appointment draft not found with id: " + id
                                )
                        );

        // Don't modify completed/abandoned drafts.
        if (!"FILLING".equals(draft.getStatus())) {
            throw new RuntimeException(
                    "Appointment draft is no longer active"
            );
        }

        draft.setPatientName(patientName);
        draft.setPhone(phone);
        draft.setLastActiveAt(LocalDateTime.now());

        return appointmentDraftRepository.save(draft);
    }
}