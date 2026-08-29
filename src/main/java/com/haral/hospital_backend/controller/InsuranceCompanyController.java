package com.haral.hospital_backend.controller;

import com.haral.hospital_backend.entity.InsuranceCompany;
import com.haral.hospital_backend.service.InsuranceCompanyService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/insurance")
@CrossOrigin(origins = "http://localhost:5173")
public class InsuranceCompanyController {

    private final InsuranceCompanyService
            insuranceCompanyService;

    public InsuranceCompanyController(
            InsuranceCompanyService insuranceCompanyService
    ) {
        this.insuranceCompanyService =
                insuranceCompanyService;
    }

    // =====================================================
    // PUBLIC — ACTIVE COMPANIES
    // =====================================================

    @GetMapping
    public ResponseEntity<List<InsuranceCompany>>
    getActiveCompanies() {

        return ResponseEntity.ok(
                insuranceCompanyService
                        .getActiveCompanies()
        );
    }

    // =====================================================
    // ADMIN — ALL COMPANIES
    // =====================================================

    @GetMapping("/all")
    public ResponseEntity<List<InsuranceCompany>>
    getAllCompanies() {

        return ResponseEntity.ok(
                insuranceCompanyService
                        .getAllCompanies()
        );
    }

    // =====================================================
    // ADMIN — CREATE
    // =====================================================

    @PostMapping
    public ResponseEntity<InsuranceCompany>
    createCompany(
            @RequestBody InsuranceCompany company
    ) {

        return ResponseEntity.ok(
                insuranceCompanyService
                        .createCompany(company)
        );
    }

    // =====================================================
    // ADMIN — UPDATE
    // =====================================================

    @PutMapping("/{id}")
    public ResponseEntity<InsuranceCompany>
    updateCompany(
            @PathVariable Long id,
            @RequestBody InsuranceCompany company
    ) {

        return ResponseEntity.ok(
                insuranceCompanyService
                        .updateCompany(id, company)
        );
    }

    // =====================================================
    // ADMIN — TOGGLE
    // =====================================================

    @PatchMapping("/{id}/toggle")
    public ResponseEntity<InsuranceCompany>
    toggleCompany(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                insuranceCompanyService
                        .toggleCompany(id)
        );
    }

    // =====================================================
    // ADMIN — DELETE
    // =====================================================

    @DeleteMapping("/{id}")
    public ResponseEntity<Void>
    deleteCompany(
            @PathVariable Long id
    ) {

        insuranceCompanyService
                .deleteCompany(id);

        return ResponseEntity
                .noContent()
                .build();
    }
}