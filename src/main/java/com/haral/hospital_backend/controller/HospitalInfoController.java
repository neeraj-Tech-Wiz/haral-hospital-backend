package com.haral.hospital_backend.controller;

import com.haral.hospital_backend.entity.HospitalInfo;
import com.haral.hospital_backend.service.HospitalInfoService;
import com.haral.hospital_backend.service.SupabaseStorageService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/hospital-info")
@CrossOrigin(origins = "http://localhost:5173")
public class HospitalInfoController {

    private final HospitalInfoService hospitalInfoService;
    private final SupabaseStorageService supabaseStorageService;

    public HospitalInfoController(
            HospitalInfoService hospitalInfoService,
            SupabaseStorageService supabaseStorageService) {

        this.hospitalInfoService = hospitalInfoService;
        this.supabaseStorageService = supabaseStorageService;
    }

    // =====================================================
    // PUBLIC — GET HOSPITAL INFORMATION
    // =====================================================

    @GetMapping
    public ResponseEntity<?> getHospitalInfo() {

        HospitalInfo hospitalInfo =
                hospitalInfoService.getHospitalInfo();

        if (hospitalInfo == null) {

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of(
                            "success", false,
                            "message",
                            "Hospital information not available"
                    ));
        }

        return ResponseEntity.ok(hospitalInfo);
    }

    // =====================================================
    // ADMIN — CREATE
    // =====================================================

    @PostMapping
    public ResponseEntity<?> saveHospitalInfo(
            @RequestBody HospitalInfo hospitalInfo) {

        HospitalInfo saved =
                hospitalInfoService.saveHospitalInfo(
                        hospitalInfo
                );

        return ResponseEntity.ok(saved);
    }

    // =====================================================
    // ADMIN — UPDATE
    // =====================================================

    @PutMapping
    public ResponseEntity<?> updateHospitalInfo(
            @RequestBody HospitalInfo hospitalInfo) {

        HospitalInfo updated =
                hospitalInfoService.saveHospitalInfo(
                        hospitalInfo
                );

        return ResponseEntity.ok(updated);
    }

    // =====================================================
    // ADMIN — UPLOAD HOSPITAL IMAGE
    // =====================================================

    @PostMapping("/image")
    public ResponseEntity<?> uploadHospitalImage(
            @RequestParam("file") MultipartFile file) {

        try {

            String imageUrl =
                    supabaseStorageService.uploadHospitalImage(
                            file
                    );

            HospitalInfo hospitalInfo =
                    hospitalInfoService.getHospitalInfo();

            if (hospitalInfo == null) {

                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(Map.of(
                                "success", false,
                                "message",
                                "Hospital information must be created before uploading an image"
                        ));
            }

            hospitalInfo.setImageUrl(imageUrl);

            HospitalInfo updated =
                    hospitalInfoService.saveHospitalInfo(
                            hospitalInfo
                    );

            return ResponseEntity.ok(
                    Map.of(
                            "success", true,
                            "message",
                            "Hospital image uploaded successfully",
                            "imageUrl",
                            imageUrl,
                            "hospitalInfo",
                            updated
                    )
            );

        } catch (IllegalArgumentException e) {

            return ResponseEntity
                    .badRequest()
                    .body(Map.of(
                            "success", false,
                            "message",
                            e.getMessage()
                    ));

        } catch (Exception e) {

            e.printStackTrace();

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "success", false,
                            "message",
                            "Hospital image upload failed"
                    ));
        }
    }

    // =====================================================
    // ADMIN — DELETE
    // =====================================================

    @DeleteMapping
    public ResponseEntity<?> deleteHospitalInfo() {

        hospitalInfoService.deleteHospitalInfo();

        return ResponseEntity.ok(
                Map.of(
                        "success", true,
                        "message",
                        "Hospital information deleted successfully"
                )
        );
    }
}