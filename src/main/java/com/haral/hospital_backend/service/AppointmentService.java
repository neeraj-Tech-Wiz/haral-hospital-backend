package com.haral.hospital_backend.service;

import com.haral.hospital_backend.entity.Appointment;
import com.haral.hospital_backend.repository.AppointmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;

    public AppointmentService(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    // Create a new appointment
    public Appointment createAppointment(Appointment appointment) {
        appointment.setStatus("PENDING");
        return appointmentRepository.save(appointment);
    }

    // Get all appointments
    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    // Get appointment by ID
    public Optional<Appointment> getAppointmentById(Long id) {
        return appointmentRepository.findById(id);
    }

    // Update appointment status
    public Appointment updateStatus(Long id, String status) {

        Appointment appointment = appointmentRepository
                .findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Appointment not found with id: " + id
                        )
                );

        // Allow only valid appointment statuses
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

    // Delete appointment
    public void deleteAppointment(Long id) {

        if (!appointmentRepository.existsById(id)) {
            throw new RuntimeException(
                    "Appointment not found with id: " + id
            );
        }

        appointmentRepository.deleteById(id);
    }
}