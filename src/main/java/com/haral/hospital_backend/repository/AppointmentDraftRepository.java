package com.haral.hospital_backend.repository;

import com.haral.hospital_backend.entity.AppointmentDraft;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AppointmentDraftRepository
        extends JpaRepository<AppointmentDraft, Long> {

    Optional<AppointmentDraft> findFirstByPhoneAndStatus(
            String phone,
            String status
    );

    List<AppointmentDraft> findByStatusOrderByLastActiveAtDesc(
            String status
    );

    List<AppointmentDraft> findAllByOrderByLastActiveAtDesc();

    List<AppointmentDraft> findByStatusAndLastActiveAtBefore(
            String status,
            LocalDateTime time
    );
}