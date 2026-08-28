package com.haral.hospital_backend.controller;

import com.haral.hospital_backend.entity.Facility;
import com.haral.hospital_backend.service.FacilityService;
import com.haral.hospital_backend.service.SupabaseStorageService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/facilities")
@CrossOrigin(origins = "http://localhost:5173")
public class FacilityController {

    private final FacilityService facilityService;
    private final SupabaseStorageService supabaseStorageService;

    public FacilityController(
            FacilityService facilityService,
            SupabaseStorageService supabaseStorageService
    ) {
        this.facilityService = facilityService;
        this.supabaseStorageService = supabaseStorageService;
    }


    // =========================================================
    // PUBLIC - GET ACTIVE FACILITIES
    // =========================================================

    @GetMapping
    public ResponseEntity<List<Facility>> getActiveFacilities() {

        return ResponseEntity.ok(
                facilityService.getActiveFacilities()
        );
    }


    // =========================================================
    // ADMIN - GET ALL FACILITIES
    // Includes active + hidden
    // =========================================================

    @GetMapping("/all")
    public ResponseEntity<List<Facility>> getAllFacilities() {

        return ResponseEntity.ok(
                facilityService.getAllFacilities()
        );
    }


    // =========================================================
    // ADMIN - CREATE FACILITY
    // =========================================================

    @PostMapping
    public ResponseEntity<Facility> createFacility(
            @RequestBody Facility facility
    ) {

        return ResponseEntity.ok(
                facilityService.createFacility(facility)
        );
    }


    // =========================================================
    // ADMIN - UPDATE FACILITY
    // =========================================================

    @PutMapping("/{id}")
    public ResponseEntity<Facility> updateFacility(
            @PathVariable Long id,
            @RequestBody Facility facility
    ) {

        return ResponseEntity.ok(
                facilityService.updateFacility(id, facility)
        );
    }


    // =========================================================
    // ADMIN - TOGGLE ACTIVE / INACTIVE
    // =========================================================

    @PatchMapping("/{id}/toggle")
    public ResponseEntity<Facility> toggleFacility(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                facilityService.toggleFacility(id)
        );
    }


    // =========================================================
    // ADMIN - DELETE FACILITY
    // =========================================================

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFacility(
            @PathVariable Long id
    ) {

        facilityService.deleteFacility(id);

        return ResponseEntity.noContent().build();
    }


    // =========================================================
    // ADMIN - UPLOAD FACILITY IMAGE
    // =========================================================

    @PostMapping("/{id}/image")
    public ResponseEntity<?> uploadFacilityImage(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file
    ) {

        try {

            // Check that facility exists
            Facility facility =
                    facilityService.getFacilityById(id);


            // Upload image to Supabase
            String imageUrl =
                    supabaseStorageService.uploadFacilityImage(
                            file,
                            id
                    );


            // Save image URL in database
            facility.setImageUrl(imageUrl);

            Facility updatedFacility =
                    facilityService.saveFacility(facility);


            return ResponseEntity.ok(
                    Map.of(
                            "success", true,
                            "message",
                            "Facility image uploaded successfully",
                            "imageUrl",
                            imageUrl,
                            "facility",
                            updatedFacility
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