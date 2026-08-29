package com.haral.hospital_backend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "specialities")
public class Speciality {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // =====================================================
    // SPECIALITY NAME
    // =====================================================

    @Column(nullable = false)
    private String name;

    // =====================================================
    // MARATHI NAME
    // =====================================================

    @Column(nullable = false)
    private String marathi;

    // =====================================================
    // DESCRIPTION
    // =====================================================

    @Column(columnDefinition = "TEXT")
    private String description;

    // =====================================================
    // LUCIDE ICON NAME
    // Example: HeartPulse, Bone, Baby, Brain
    // =====================================================

    @Column(nullable = false)
    private String icon;

    // =====================================================
    // DISPLAY ORDER
    // Controls the order shown on the website
    // =====================================================

    @Column(nullable = false)
    private Integer displayOrder = 0;

    // =====================================================
    // ACTIVE / HIDDEN
    // =====================================================

    @Column(nullable = false)
    private boolean active = true;

    @Column(columnDefinition = "TEXT")
    private String detailedDescription;

    @Column(columnDefinition = "TEXT")
    private String marathiDetailedDescription;

    @Column(columnDefinition = "TEXT")
    private String overview;

    @Column(columnDefinition = "TEXT")
    private String marathiOverview;

    @Column(columnDefinition = "TEXT")
    private String conditionsTreated;

    @Column(columnDefinition = "TEXT")
    private String marathiConditionsTreated;

    @Column(columnDefinition = "TEXT")
    private String services;

    @Column(columnDefinition = "TEXT")
    private String marathiServices;

    @Column
    private String imageUrl;


    // =====================================================
    // CONSTRUCTORS
    // =====================================================

    public Speciality() {
    }


    public Speciality(
            String name,
            String marathi,
            String description,
            String icon,
            Integer displayOrder,
            boolean active
    ) {
        this.name = name;
        this.marathi = marathi;
        this.description = description;
        this.icon = icon;
        this.displayOrder = displayOrder;
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


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }


    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }


    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getDetailedDescription() {
        return detailedDescription;
    }

    public void setDetailedDescription(String detailedDescription) {
        this.detailedDescription = detailedDescription;
    }

    public String getMarathiDetailedDescription() {
        return marathiDetailedDescription;
    }

    public void setMarathiDetailedDescription(
            String marathiDetailedDescription) {

        this.marathiDetailedDescription =
                marathiDetailedDescription;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getMarathiOverview() {
        return marathiOverview;
    }

    public void setMarathiOverview(String marathiOverview) {
        this.marathiOverview = marathiOverview;
    }

    public String getConditionsTreated() {
        return conditionsTreated;
    }

    public void setConditionsTreated(String conditionsTreated) {
        this.conditionsTreated = conditionsTreated;
    }

    public String getMarathiConditionsTreated() {
        return marathiConditionsTreated;
    }

    public void setMarathiConditionsTreated(
            String marathiConditionsTreated) {
        this.marathiConditionsTreated =
                marathiConditionsTreated;
    }

    public String getServices() {
        return services;
    }

    public void setServices(String services) {
        this.services = services;
    }

    public String getMarathiServices() {
        return marathiServices;
    }

    public void setMarathiServices(String marathiServices) {
        this.marathiServices = marathiServices;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}