package com.haral.hospital_backend.service;

import com.haral.hospital_backend.entity.Appointment;
import com.haral.hospital_backend.repository.AppointmentRepository;
import com.haral.hospital_backend.repository.AppointmentDraftRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final AppointmentDraftService appointmentDraftService;

    public AppointmentService(
            AppointmentRepository appointmentRepository,
            AppointmentDraftService appointmentDraftService) {

        this.appointmentRepository = appointmentRepository;
        this.appointmentDraftService = appointmentDraftService;
    }


    // =====================================================
    // CREATE APPOINTMENT
    // =====================================================

    public Appointment createAppointment(
            Appointment appointment) {

        appointment.setStatus("PENDING");

        Appointment savedAppointment =
                appointmentRepository.save(appointment);

        // Patient actually submitted the appointment
        appointmentDraftService.markAsSubmitted(
                appointment.getPhone()
        );

        return savedAppointment;
    }


    // =====================================================
    // GET ALL APPOINTMENTS
    // =====================================================

    public List<Appointment> getAllAppointments() {

        return appointmentRepository.findAll();
    }


    // =====================================================
    // GET APPOINTMENT BY ID
    // =====================================================

    public Optional<Appointment> getAppointmentById(Long id) {

        return appointmentRepository.findById(id);
    }


    // =====================================================
    // UPDATE APPOINTMENT STATUS
    // =====================================================

    public Appointment updateStatus(
            Long id,
            String status) {

        Appointment appointment =
                appointmentRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Appointment not found with id: "
                                                + id
                                )
                        );


        if (!status.equals("PENDING")
                && !status.equals("CONFIRMED")
                && !status.equals("REJECTED")) {

            throw new RuntimeException(
                    "Invalid appointment status"
            );
        }

        appointment.setStatus(status);

        return appointmentRepository.save(appointment);
    }


    // =====================================================
    // DELETE APPOINTMENT
    // =====================================================

    public void deleteAppointment(Long id) {

        if (!appointmentRepository.existsById(id)) {

            throw new RuntimeException(
                    "Appointment not found with id: " + id
            );
        }

        appointmentRepository.deleteById(id);
    }
}