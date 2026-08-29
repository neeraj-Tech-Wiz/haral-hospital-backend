package com.haral.hospital_backend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "insurance_companies")
public class InsuranceCompany {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String marathi;

    @Column(nullable = false)
    private boolean active = true;

    // =====================================================
    // CONSTRUCTORS
    // =====================================================

    public InsuranceCompany() {
    }

    public InsuranceCompany(
            String name,
            String marathi,
            boolean active
    ) {
        this.name = name;
        this.marathi = marathi;
        this.active = active;
    }

    // =====================================================
    // GETTERS & SETTERS
    // =====================================================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMarathi() {
        return marathi;
    }

    public void setMarathi(String marathi) {
        this.marathi = marathi;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}