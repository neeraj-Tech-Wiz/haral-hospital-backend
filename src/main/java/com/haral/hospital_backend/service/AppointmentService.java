package com.haral.hospital_backend.service;

import com.haral.hospital_backend.entity.Appointment;
import com.haral.hospital_backend.repository.AppointmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final AppointmentDraftService appointmentDraftService;
    private final WhatsAppService whatsAppService;

    public AppointmentService(
            AppointmentRepository appointmentRepository,
            AppointmentDraftService appointmentDraftService,
            WhatsAppService whatsAppService) {

        this.appointmentRepository = appointmentRepository;
        this.appointmentDraftService = appointmentDraftService;
        this.whatsAppService = whatsAppService;
    }


    // =====================================================
    // CREATE APPOINTMENT
    // =====================================================

    public Appointment createAppointment(Appointment appointment) {

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

    public Appointment updateStatus(Long id, String status) {

        Appointment appointment =
                appointmentRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Appointment not found with id: " + id
                                )
                        );

        // Validate status
        if (!status.equals("PENDING")
                && !status.equals("CONFIRMED")
                && !status.equals("REJECTED")) {

            throw new RuntimeException(
                    "Invalid appointment status"
            );
        }

        // Send WhatsApp only when status changes
        // from something else to CONFIRMED
        boolean shouldSendWhatsApp =
                status.equals("CONFIRMED")
                        && !appointment.getStatus().equals("CONFIRMED");

        appointment.setStatus(status);

        Appointment savedAppointment =
                appointmentRepository.save(appointment);

        // Send appointment confirmation WhatsApp
        if (shouldSendWhatsApp) {

            whatsAppService.sendAppointmentConfirmation(
                    savedAppointment
            );
        }

        return savedAppointment;
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