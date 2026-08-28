package com.haral.hospital_backend.repository;

import com.haral.hospital_backend.entity.HospitalInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HospitalInfoRepository
        extends JpaRepository<HospitalInfo, Long> {
}