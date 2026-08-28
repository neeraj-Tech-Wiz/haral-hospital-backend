package com.haral.hospital_backend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "hospital_info")
public class HospitalInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String hospitalName;

    @Column(nullable = false)
    private String tagline;

    @Column(columnDefinition = "TEXT")
    private String about;

    @Column(nullable = false)
    private String emergencyNumber;

    private String phone;

    private String email;

    @Column(columnDefinition = "TEXT")
    private String address;

    @Column(columnDefinition = "TEXT")
    private String workingHours;

    private String imageUrl;

    // =====================================================
    // CONSTRUCTORS
    // =====================================================

    public HospitalInfo() {
    }

    public HospitalInfo(
            String hospitalName,
            String tagline,
            String about,
            String emergencyNumber,
            String phone,
            String email,
            String address,
            String workingHours,
            String imageUrl
    ) {
        this.hospitalName = hospitalName;
        this.tagline = tagline;
        this.about = about;
        this.emergencyNumber = emergencyNumber;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.workingHours = workingHours;
        this.imageUrl = imageUrl;
    }

    // =====================================================
    // GETTERS & SETTERS
    // =====================================================

    public Long getId() {
        return id;
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    public String getTagline() {
        return tagline;
    }

    public void setTagline(String tagline) {
        this.tagline = tagline;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getEmergencyNumber() {
        return emergencyNumber;
    }

    public void setEmergencyNumber(String emergencyNumber) {
        this.emergencyNumber = emergencyNumber;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getWorkingHours() {
        return workingHours;
    }

    public void setWorkingHours(String workingHours) {
        this.workingHours = workingHours;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}