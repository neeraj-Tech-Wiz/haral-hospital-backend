package com.haral.hospital_backend.controller;

import com.haral.hospital_backend.entity.Doctor;
import com.haral.hospital_backend.repository.DoctorRepository;
import com.haral.hospital_backend.service.SupabaseStorageService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/doctors")
@CrossOrigin(origins = "http://localhost:5173")
public class DoctorImageController {

    private final DoctorRepository doctorRepository;
    private final SupabaseStorageService storageService;

    public DoctorImageController(
            DoctorRepository doctorRepository,
            SupabaseStorageService storageService
    ) {
        this.doctorRepository = doctorRepository;
        this.storageService = storageService;
    }

    @PostMapping("/{id}/image")
    public ResponseEntity<?> uploadDoctorImage(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file
    ) {

        try {

            Doctor doctor = doctorRepository
                    .findById(id)
                    .orElse(null);

            if (doctor == null) {
                return ResponseEntity
                        .notFound()
                        .build();
            }

            String imageUrl =
                    storageService.uploadDoctorImage(id, file);

            doctor.setImageUrl(imageUrl);

            doctorRepository.save(doctor);

            return ResponseEntity.ok(
                    Map.of(
                            "success", true,
                            "message", "Doctor image uploaded successfully",
                            "imageUrl", imageUrl
                    )
            );

        } catch (IllegalArgumentException e) {

            return ResponseEntity
                    .badRequest()
                    .body(
                            Map.of(
                                    "success", false,
                                    "message", e.getMessage()
                            )
                    );

        } catch (Exception e) {

            e.printStackTrace();

            return ResponseEntity
                    .internalServerError()
                    .body(
                            Map.of(
                                    "success", false,
                                    "message", "Failed to upload doctor image"
                            )
                    );
        }
    }
}