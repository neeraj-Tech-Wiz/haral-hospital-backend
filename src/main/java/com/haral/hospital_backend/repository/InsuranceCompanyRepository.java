package com.haral.hospital_backend.repository;

import com.haral.hospital_backend.entity.InsuranceCompany;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InsuranceCompanyRepository
        extends JpaRepository<InsuranceCompany, Long> {

    List<InsuranceCompany> findByActiveTrue();

}