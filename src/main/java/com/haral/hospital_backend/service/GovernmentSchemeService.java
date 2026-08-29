package com.haral.hospital_backend.service;

import com.haral.hospital_backend.entity.GovernmentScheme;
import com.haral.hospital_backend.repository.GovernmentSchemeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GovernmentSchemeService {

    private final GovernmentSchemeRepository
            governmentSchemeRepository;

    public GovernmentSchemeService(
            GovernmentSchemeRepository governmentSchemeRepository
    ) {
        this.governmentSchemeRepository =
                governmentSchemeRepository;
    }


    // =====================================================
    // PUBLIC — ACTIVE SCHEMES
    // =====================================================

    public List<GovernmentScheme>
    getActiveSchemes() {

        return governmentSchemeRepository
                .findByActiveTrue();
    }


    // =====================================================
    // ADMIN — ALL SCHEMES
    // =====================================================

    public List<GovernmentScheme>
    getAllSchemes() {

        return governmentSchemeRepository.findAll();
    }


    // =====================================================
    // CREATE
    // =====================================================

    public GovernmentScheme
    createScheme(
            GovernmentScheme scheme
    ) {

        scheme.setId(null);

        return governmentSchemeRepository.save(
                scheme
        );
    }


    // =====================================================
    // UPDATE
    // =====================================================

    public GovernmentScheme
    updateScheme(
            Long id,
            GovernmentScheme scheme
    ) {

        GovernmentScheme existing =
                governmentSchemeRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Government scheme not found"
                                )
                        );

        existing.setName(
                scheme.getName()
        );

        existing.setMarathi(
                scheme.getMarathi()
        );

        existing.setSubtitle(
                scheme.getSubtitle()
        );

        existing.setMarathiSubtitle(
                scheme.getMarathiSubtitle()
        );

        existing.setDescription(
                scheme.getDescription()
        );

        existing.setMarathiDescription(
                scheme.getMarathiDescription()
        );

        existing.setBenefit(
                scheme.getBenefit()
        );

        existing.setMarathiBenefit(
                scheme.getMarathiBenefit()
        );

        existing.setCoverage(
                scheme.getCoverage()
        );

        existing.setCoverageLabel(
                scheme.getCoverageLabel()
        );

        existing.setMarathiCoverageLabel(
                scheme.getMarathiCoverageLabel()
        );

        existing.setActive(
                scheme.isActive()
        );

        return governmentSchemeRepository.save(
                existing
        );
    }


    // =====================================================
    // TOGGLE
    // =====================================================

    public GovernmentScheme
    toggleScheme(Long id) {

        GovernmentScheme scheme =
                governmentSchemeRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Government scheme not found"
                                )
                        );

        scheme.setActive(
                !scheme.isActive()
        );

        return governmentSchemeRepository.save(
                scheme
        );
    }


    // =====================================================
    // DELETE
    // =====================================================

    public void deleteScheme(Long id) {

        if (!governmentSchemeRepository
                .existsById(id)) {

            throw new RuntimeException(
                    "Government scheme not found"
            );
        }

        governmentSchemeRepository.deleteById(id);
    }
    public GovernmentScheme saveScheme(
            GovernmentScheme scheme
    ) {
        return governmentSchemeRepository.save(scheme);
    }
}