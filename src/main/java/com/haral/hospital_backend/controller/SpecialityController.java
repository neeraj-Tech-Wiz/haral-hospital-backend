package com.haral.hospital_backend.controller;

import com.haral.hospital_backend.entity.Speciality;
import com.haral.hospital_backend.service.SpecialityService;
import com.haral.hospital_backend.service.SupabaseStorageService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/specialities")
@CrossOrigin(origins = "http://localhost:5173")
public class SpecialityController {

    private final SpecialityService specialityService;
    private final SupabaseStorageService supabaseStorageService;

    public SpecialityController(
            SpecialityService specialityService,
            SupabaseStorageService supabaseStorageService) {

        this.specialityService = specialityService;
        this.supabaseStorageService = supabaseStorageService;
    }


    // =====================================================
    // PUBLIC — ACTIVE SPECIALITIES
    // =====================================================

    @GetMapping
    public ResponseEntity<List<Speciality>>
    getActiveSpecialities() {

        return ResponseEntity.ok(
                specialityService
                        .getActiveSpecialities()
        );
    }


    // =====================================================
    // PUBLIC — SINGLE SPECIALITY
    // =====================================================

    @GetMapping("/{id}")
    public ResponseEntity<?> getSpecialityById(
            @PathVariable Long id) {

        try {

            Speciality speciality =
                    specialityService
                            .getSpecialityById(id);

            // Do not expose hidden speciality publicly
            if (!speciality.isActive()) {

                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(
                                Map.of(
                                        "success", false,
                                        "message",
                                        "Speciality not available"
                                )
                        );
            }

            return ResponseEntity.ok(
                    speciality
            );

        } catch (RuntimeException e) {

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(
                            Map.of(
                                    "success", false,
                                    "message",
                                    e.getMessage()
                            )
                    );
        }
    }


    // =====================================================
    // ADMIN — ALL SPECIALITIES
    // =====================================================

    @GetMapping("/all")
    public ResponseEntity<List<Speciality>>
    getAllSpecialities() {

        return ResponseEntity.ok(
                specialityService
                        .getAllSpecialities()
        );
    }


    // =====================================================
    // ADMIN — CREATE
    // =====================================================

    @PostMapping
    public ResponseEntity<Speciality>
    createSpeciality(
            @RequestBody Speciality speciality) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        specialityService
                                .createSpeciality(
                                        speciality
                                )
                );
    }


    // =====================================================
    // ADMIN — UPDATE
    // =====================================================

    @PutMapping("/{id}")
    public ResponseEntity<Speciality>
    updateSpeciality(
            @PathVariable Long id,
            @RequestBody Speciality speciality) {

        return ResponseEntity.ok(
                specialityService
                        .updateSpeciality(
                                id,
                                speciality
                        )
        );
    }


    // =====================================================
    // ADMIN — TOGGLE
    // =====================================================

    @PatchMapping("/{id}/toggle")
    public ResponseEntity<Speciality>
    toggleSpeciality(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                specialityService
                        .toggleSpeciality(id)
        );
    }


    // =====================================================
    // ADMIN — DELETE
    // =====================================================

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSpeciality(
            @PathVariable Long id) {

        try {

            specialityService
                    .deleteSpeciality(id);

            return ResponseEntity.ok(
                    Map.of(
                            "success", true,
                            "message",
                            "Speciality deleted successfully"
                    )
            );

        } catch (RuntimeException e) {

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(
                            Map.of(
                                    "success", false,
                                    "message",
                                    e.getMessage()
                            )
                    );
        }
    }


    // =====================================================
    // ADMIN — UPLOAD SPECIALITY IMAGE
    // =====================================================

    @PostMapping("/{id}/image")
    public ResponseEntity<?> uploadSpecialityImage(
            @PathVariable Long id,
            @RequestParam("image") MultipartFile image) {

        try {

            Speciality speciality =
                    specialityService
                            .getSpecialityById(id);

            String imageUrl =
                    supabaseStorageService
                            .uploadSpecialityImage(
                                    image,
                                    id
                            );

            speciality.setImageUrl(
                    imageUrl
            );

            Speciality updated =
                    specialityService
                            .saveSpeciality(
                                    speciality
                            );

            return ResponseEntity.ok(
                    Map.of(
                            "success", true,
                            "message",
                            "Speciality image uploaded successfully",
                            "imageUrl",
                            imageUrl,
                            "speciality",
                            updated
                    )
            );

        } catch (Exception e) {

            return ResponseEntity
                    .badRequest()
                    .body(
                            Map.of(
                                    "success", false,
                                    "message",
                                    e.getMessage()
                            )
                    );
        }
    }
}