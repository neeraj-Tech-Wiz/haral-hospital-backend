package com.haral.hospital_backend.service;

import com.haral.hospital_backend.entity.AppointmentDraft;
import com.haral.hospital_backend.repository.AppointmentDraftRepository;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AppointmentDraftService {

    private final AppointmentDraftRepository appointmentDraftRepository;


    public AppointmentDraftService(
            AppointmentDraftRepository appointmentDraftRepository) {

        this.appointmentDraftRepository =
                appointmentDraftRepository;
    }


    // =====================================================
    // CREATE OR UPDATE DRAFT
    // =====================================================

    public AppointmentDraft saveDraft(
            Long draftId,
            String patientName,
            String phone) {

        LocalDateTime now =
                LocalDateTime.now();


        // =================================================
        // UPDATE EXISTING DRAFT
        // =================================================

        if (draftId != null) {

            AppointmentDraft draft =
                    appointmentDraftRepository
                            .findById(draftId)
                            .orElse(null);


            if (draft != null
                    && "FILLING".equals(
                    draft.getStatus())) {

                draft.setPatientName(patientName);

                draft.setPhone(phone);

                draft.setLastActiveAt(now);


                return appointmentDraftRepository.save(
                        draft
                );
            }
        }


        // =================================================
        // CREATE NEW DRAFT
        // =================================================

        AppointmentDraft draft =
                new AppointmentDraft();

        draft.setPatientName(patientName);

        draft.setPhone(phone);

        draft.setStatus("FILLING");

        draft.setCreatedAt(now);

        draft.setLastActiveAt(now);


        return appointmentDraftRepository.save(
                draft
        );
    }


    // =====================================================
    // GET ACTIVE DRAFTS
    // =====================================================

    public List<AppointmentDraft> getActiveDrafts() {

        return appointmentDraftRepository
                .findByStatusOrderByLastActiveAtDesc(
                        "FILLING"
                );
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

                    appointmentDraftRepository.save(
                            draft
                    );
                });
    }
    public void markAsSubmitted(Long id) {

        AppointmentDraft draft =
                appointmentDraftRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Draft not found with id: " + id
                                )
                        );

        draft.setStatus("SUBMITTED");

        draft.setLastActiveAt(
                LocalDateTime.now()
        );

        appointmentDraftRepository.save(draft);
    }
    // =====================================================
    // GET ALL ADMIN DRAFT ACTIVITY
    // =====================================================

    public List<AppointmentDraft> getAdminDrafts() {

        return appointmentDraftRepository
                .findByStatusInOrderByLastActiveAtDesc(
                        List.of("FILLING", "ABANDONED")
                );
    }
}