package com.haral.hospital_backend.service;

import com.haral.hospital_backend.entity.Speciality;
import com.haral.hospital_backend.repository.SpecialityRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpecialityService {

    private final SpecialityRepository specialityRepository;

    public SpecialityService(
            SpecialityRepository specialityRepository) {

        this.specialityRepository = specialityRepository;
    }


    // =====================================================
    // PUBLIC — ACTIVE SPECIALITIES
    // =====================================================

    public List<Speciality> getActiveSpecialities() {

        return specialityRepository
                .findByActiveTrueOrderByDisplayOrderAsc();
    }


    // =====================================================
    // ADMIN — ALL SPECIALITIES
    // =====================================================

    public List<Speciality> getAllSpecialities() {

        return specialityRepository
                .findAllByOrderByDisplayOrderAsc();
    }


    // =====================================================
    // CREATE
    // =====================================================

    public Speciality createSpeciality(
            Speciality speciality) {

        return specialityRepository.save(speciality);
    }


    // =====================================================
    // UPDATE
    // =====================================================

    // =====================================================
// UPDATE
// =====================================================

    // =====================================================
// UPDATE
// =====================================================

    public Speciality updateSpeciality(
            Long id,
            Speciality speciality) {

        Speciality existing =
                specialityRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Speciality not found with id: " + id
                                )
                        );

        // =================================================
        // BASIC INFORMATION
        // =================================================

        existing.setName(
                speciality.getName()
        );

        existing.setMarathi(
                speciality.getMarathi()
        );

        existing.setDescription(
                speciality.getDescription()
        );

        existing.setIcon(
                speciality.getIcon()
        );

        existing.setDisplayOrder(
                speciality.getDisplayOrder()
        );

        existing.setActive(
                speciality.isActive()
        );


        // =================================================
        // DEPARTMENT OVERVIEW
        // =================================================

        existing.setOverview(
                speciality.getOverview()
        );

        existing.setMarathiOverview(
                speciality.getMarathiOverview()
        );


        // =================================================
        // CONDITIONS TREATED
        // =================================================

        existing.setConditionsTreated(
                speciality.getConditionsTreated()
        );

        existing.setMarathiConditionsTreated(
                speciality.getMarathiConditionsTreated()
        );


        // =================================================
        // SERVICES & TREATMENTS
        // =================================================

        existing.setServices(
                speciality.getServices()
        );

        existing.setMarathiServices(
                speciality.getMarathiServices()
        );


        // =================================================
        // DETAILED INFORMATION
        // =================================================

        existing.setDetailedDescription(
                speciality.getDetailedDescription()
        );

        existing.setMarathiDetailedDescription(
                speciality.getMarathiDetailedDescription()
        );


        // =================================================
        // IMAGE
        // =================================================
        // Do NOT update imageUrl here.
        // Image is handled separately by the image upload API.


        return specialityRepository.save(existing);
    }


    // =====================================================
    // SAVE
    // Used by image upload controller
    // =====================================================

    public Speciality saveSpeciality(
            Speciality speciality) {

        return specialityRepository.save(
                speciality
        );
    }


    // =====================================================
    // GET BY ID
    // Used by public speciality detail page
    // =====================================================

    public Speciality getSpecialityById(
            Long id) {

        return specialityRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Speciality not found with id: " + id
                        )
                );
    }


    // =====================================================
    // TOGGLE ACTIVE / HIDDEN
    // =====================================================

    public Speciality toggleSpeciality(
            Long id) {

        Speciality speciality =
                specialityRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Speciality not found with id: " + id
                                )
                        );

        speciality.setActive(
                !speciality.isActive()
        );

        return specialityRepository.save(
                speciality
        );
    }


    // =====================================================
    // DELETE
    // =====================================================

    public void deleteSpeciality(
            Long id) {

        if (!specialityRepository.existsById(id)) {

            throw new RuntimeException(
                    "Speciality not found with id: " + id
            );
        }

        specialityRepository.deleteById(id);
    }
}