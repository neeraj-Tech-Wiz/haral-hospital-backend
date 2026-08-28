package com.haral.hospital_backend.service;

import com.haral.hospital_backend.entity.HospitalInfo;
import com.haral.hospital_backend.repository.HospitalInfoRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HospitalInfoService {

    private final HospitalInfoRepository hospitalInfoRepository;

    public HospitalInfoService(
            HospitalInfoRepository hospitalInfoRepository) {

        this.hospitalInfoRepository = hospitalInfoRepository;
    }

    // =====================================================
    // GET HOSPITAL INFORMATION
    // =====================================================

    public HospitalInfo getHospitalInfo() {

        List<HospitalInfo> records =
                hospitalInfoRepository.findAll();

        if (records.isEmpty()) {
            return null;
        }

        // We maintain one hospital information record.
        return records.get(0);
    }

    // =====================================================
    // CREATE / UPDATE HOSPITAL INFORMATION
    // =====================================================

    public HospitalInfo saveHospitalInfo(
            HospitalInfo hospitalInfo) {

        List<HospitalInfo> records =
                hospitalInfoRepository.findAll();

        // -------------------------------------------------
        // If record already exists, update that record
        // -------------------------------------------------

        if (!records.isEmpty()) {

            HospitalInfo existing =
                    records.get(0);

            existing.setHospitalName(
                    hospitalInfo.getHospitalName()
            );

            existing.setTagline(
                    hospitalInfo.getTagline()
            );

            existing.setAbout(
                    hospitalInfo.getAbout()
            );

            existing.setEmergencyNumber(
                    hospitalInfo.getEmergencyNumber()
            );

            existing.setPhone(
                    hospitalInfo.getPhone()
            );

            existing.setEmail(
                    hospitalInfo.getEmail()
            );

            existing.setAddress(
                    hospitalInfo.getAddress()
            );

            existing.setWorkingHours(
                    hospitalInfo.getWorkingHours()
            );

            existing.setImageUrl(
                    hospitalInfo.getImageUrl()
            );

            return hospitalInfoRepository.save(existing);
        }

        // -------------------------------------------------
        // First record
        // -------------------------------------------------

        return hospitalInfoRepository.save(
                hospitalInfo
        );
    }

    // =====================================================
    // DELETE
    // =====================================================

    public void deleteHospitalInfo() {

        hospitalInfoRepository.deleteAll();
    }
}