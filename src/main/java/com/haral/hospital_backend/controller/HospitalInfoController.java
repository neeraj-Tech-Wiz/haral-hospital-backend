package com.haral.hospital_backend.controller;

import com.haral.hospital_backend.entity.HospitalInfo;
import com.haral.hospital_backend.service.HospitalInfoService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/hospital-info")
@CrossOrigin(origins = "http://localhost:5173")
public class HospitalInfoController {

    private final HospitalInfoService hospitalInfoService;

    public HospitalInfoController(
            HospitalInfoService hospitalInfoService) {

        this.hospitalInfoService = hospitalInfoService;
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
    // ADMIN — CREATE / UPDATE
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