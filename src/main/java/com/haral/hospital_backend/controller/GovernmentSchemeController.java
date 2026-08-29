package com.haral.hospital_backend.controller;

import com.haral.hospital_backend.entity.GovernmentScheme;
import com.haral.hospital_backend.service.GovernmentSchemeService;

import com.haral.hospital_backend.service.SupabaseStorageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/government-schemes")
@CrossOrigin(origins = "http://localhost:5173")
public class GovernmentSchemeController {

    private final GovernmentSchemeService
            governmentSchemeService;
    private final SupabaseStorageService supabaseStorageService;

    public GovernmentSchemeController(
            GovernmentSchemeService governmentSchemeService,
            SupabaseStorageService supabaseStorageService
    ) {
        this.governmentSchemeService =
                governmentSchemeService;

        this.supabaseStorageService =
                supabaseStorageService;
    }


    // =====================================================
    // PUBLIC — ACTIVE SCHEMES
    // =====================================================

    @GetMapping
    public ResponseEntity<List<GovernmentScheme>>
    getActiveSchemes() {

        return ResponseEntity.ok(
                governmentSchemeService
                        .getActiveSchemes()
        );
    }


    // =====================================================
    // ADMIN — ALL SCHEMES
    // =====================================================

    @GetMapping("/all")
    public ResponseEntity<List<GovernmentScheme>>
    getAllSchemes() {

        return ResponseEntity.ok(
                governmentSchemeService
                        .getAllSchemes()
        );
    }


    // =====================================================
    // ADMIN — CREATE
    // =====================================================

    @PostMapping
    public ResponseEntity<GovernmentScheme>
    createScheme(
            @RequestBody GovernmentScheme scheme
    ) {

        return ResponseEntity.ok(
                governmentSchemeService
                        .createScheme(scheme)
        );
    }


    // =====================================================
    // ADMIN — UPDATE
    // =====================================================

    @PutMapping("/{id}")
    public ResponseEntity<GovernmentScheme>
    updateScheme(
            @PathVariable Long id,
            @RequestBody GovernmentScheme scheme
    ) {

        return ResponseEntity.ok(
                governmentSchemeService
                        .updateScheme(id, scheme)
        );
    }


    // =====================================================
    // ADMIN — TOGGLE
    // =====================================================

    @PatchMapping("/{id}/toggle")
    public ResponseEntity<GovernmentScheme>
    toggleScheme(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                governmentSchemeService
                        .toggleScheme(id)
        );
    }


    // =====================================================
    // ADMIN — DELETE
    // =====================================================

    @DeleteMapping("/{id}")
    public ResponseEntity<Void>
    deleteScheme(
            @PathVariable Long id
    ) {

        governmentSchemeService
                .deleteScheme(id);

        return ResponseEntity
                .noContent()
                .build();
    }
    @PostMapping("/{id}/image")
    public ResponseEntity<?> uploadGovernmentSchemeImage(
            @PathVariable Long id,
            @RequestParam("image") MultipartFile image
    ) {

        try {

            GovernmentScheme scheme =
                    governmentSchemeService
                            .getAllSchemes()
                            .stream()
                            .filter(item ->
                                    item.getId().equals(id)
                            )
                            .findFirst()
                            .orElseThrow(() ->
                                    new RuntimeException(
                                            "Government scheme not found"
                                    )
                            );

            String imageUrl =
                    supabaseStorageService
                            .uploadGovernmentSchemeImage(
                                    image,
                                    id
                            );

            scheme.setImageUrl(imageUrl);

            GovernmentScheme updated =
                    governmentSchemeService
                            .saveScheme(scheme);

            return ResponseEntity.ok(
                    Map.of(
                            "success", true,
                            "message",
                            "Government scheme image uploaded successfully",
                            "imageUrl",
                            imageUrl,
                            "scheme",
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