package com.haral.hospital_backend.controller;

import com.haral.hospital_backend.entity.Doctor;
import com.haral.hospital_backend.repository.DoctorRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doctors")
@CrossOrigin(origins = "http://localhost:5173")
public class DoctorController {

    private final DoctorRepository doctorRepository;

    public DoctorController(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    // Get all doctors
    @GetMapping
    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }

    // Get only active doctors
    @GetMapping("/active")
    public List<Doctor> getActiveDoctors() {
        return doctorRepository.findByActiveTrue();
    }

    // Get doctor by ID
    @GetMapping("/{id}")
    public ResponseEntity<Doctor> getDoctorById(@PathVariable Long id) {

        return doctorRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Add doctor
    @PostMapping
    public Doctor createDoctor(@RequestBody Doctor doctor) {
        return doctorRepository.save(doctor);
    }

    // Update doctor
    @PutMapping("/{id}")
    public ResponseEntity<Doctor> updateDoctor(
            @PathVariable Long id,
            @RequestBody Doctor updatedDoctor) {

        return doctorRepository.findById(id)
                .map(existingDoctor -> {

                    existingDoctor.setName(updatedDoctor.getName());
                    existingDoctor.setSpeciality(updatedDoctor.getSpeciality());
                    existingDoctor.setQualification(updatedDoctor.getQualification());
                    existingDoctor.setExperience(updatedDoctor.getExperience());
                    existingDoctor.setDescription(updatedDoctor.getDescription());
                    existingDoctor.setImageUrl(updatedDoctor.getImageUrl());
                    existingDoctor.setActive(updatedDoctor.isActive());

                    return ResponseEntity.ok(
                            doctorRepository.save(existingDoctor)
                    );

                })
                .orElse(ResponseEntity.notFound().build());
    }

    // Delete doctor
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDoctor(@PathVariable Long id) {

        if (!doctorRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        doctorRepository.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}