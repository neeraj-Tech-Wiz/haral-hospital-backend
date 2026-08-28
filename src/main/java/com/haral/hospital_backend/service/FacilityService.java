package com.haral.hospital_backend.service;

import com.haral.hospital_backend.entity.Facility;
import com.haral.hospital_backend.repository.FacilityRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FacilityService {

    private final FacilityRepository facilityRepository;

    public FacilityService(FacilityRepository facilityRepository) {
        this.facilityRepository = facilityRepository;
    }

    public List<Facility> getAllFacilities() {
        return facilityRepository.findAll();
    }

    public List<Facility> getActiveFacilities() {
        return facilityRepository.findByActiveTrue();
    }

    public Facility getFacilityById(Long id) {
        return facilityRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Facility not found with id: " + id)
                );
    }

    public Facility createFacility(Facility facility) {
        return facilityRepository.save(facility);
    }

    public Facility updateFacility(Long id, Facility updatedFacility) {

        Facility existing = getFacilityById(id);

        existing.setName(updatedFacility.getName());
        existing.setMarathi(updatedFacility.getMarathi());
        existing.setDescription(updatedFacility.getDescription());
        existing.setImageUrl(updatedFacility.getImageUrl());
        existing.setActive(updatedFacility.isActive());

        return facilityRepository.save(existing);
    }

    public void deleteFacility(Long id) {

        Facility existing = getFacilityById(id);

        facilityRepository.delete(existing);
    }

    public Facility toggleFacility(Long id) {

        Facility facility = getFacilityById(id);

        facility.setActive(!facility.isActive());

        return facilityRepository.save(facility);
    }
    public Facility saveFacility(Facility facility) {
        return facilityRepository.save(facility);
    }
}