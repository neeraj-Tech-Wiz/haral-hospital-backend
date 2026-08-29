package com.haral.hospital_backend.service;

import com.haral.hospital_backend.entity.InsuranceCompany;
import com.haral.hospital_backend.repository.InsuranceCompanyRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InsuranceCompanyService {

    private final InsuranceCompanyRepository
            insuranceCompanyRepository;

    public InsuranceCompanyService(
            InsuranceCompanyRepository insuranceCompanyRepository
    ) {
        this.insuranceCompanyRepository =
                insuranceCompanyRepository;
    }

    // =====================================================
    // PUBLIC — ACTIVE COMPANIES
    // =====================================================

    public List<InsuranceCompany>
    getActiveCompanies() {

        return insuranceCompanyRepository
                .findByActiveTrue();
    }

    // =====================================================
    // ADMIN — ALL COMPANIES
    // =====================================================

    public List<InsuranceCompany>
    getAllCompanies() {

        return insuranceCompanyRepository.findAll();
    }

    // =====================================================
    // CREATE
    // =====================================================

    public InsuranceCompany
    createCompany(
            InsuranceCompany company
    ) {

        company.setId(null);

        return insuranceCompanyRepository.save(
                company
        );
    }

    // =====================================================
    // UPDATE
    // =====================================================

    public InsuranceCompany
    updateCompany(
            Long id,
            InsuranceCompany company
    ) {

        InsuranceCompany existing =
                insuranceCompanyRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Insurance company not found"
                                )
                        );

        existing.setName(
                company.getName()
        );

        existing.setMarathi(
                company.getMarathi()
        );

        existing.setActive(
                company.isActive()
        );

        return insuranceCompanyRepository.save(
                existing
        );
    }

    // =====================================================
    // TOGGLE ACTIVE
    // =====================================================

    public InsuranceCompany
    toggleCompany(Long id) {

        InsuranceCompany company =
                insuranceCompanyRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Insurance company not found"
                                )
                        );

        company.setActive(
                !company.isActive()
        );

        return insuranceCompanyRepository.save(
                company
        );
    }

    // =====================================================
    // DELETE
    // =====================================================

    public void deleteCompany(Long id) {

        if (!insuranceCompanyRepository
                .existsById(id)) {

            throw new RuntimeException(
                    "Insurance company not found"
            );
        }

        insuranceCompanyRepository.deleteById(id);
    }
}