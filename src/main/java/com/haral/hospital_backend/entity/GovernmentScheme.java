package com.haral.hospital_backend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "government_schemes")
public class GovernmentScheme {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String marathi;

    @Column(columnDefinition = "TEXT")
    private String subtitle;

    @Column(columnDefinition = "TEXT")
    private String marathiSubtitle;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "TEXT")
    private String marathiDescription;

    @Column(columnDefinition = "TEXT")
    private String benefit;

    @Column(columnDefinition = "TEXT")
    private String marathiBenefit;

    @Column
    private String coverage;

    @Column
    private String coverageLabel;

    @Column
    private String marathiCoverageLabel;

    // =====================================================
    // IMAGE
    // =====================================================

    @Column
    private String imageUrl;

    @Column(nullable = false)
    private boolean active = true;


    // =====================================================
    // CONSTRUCTORS
    // =====================================================

    public GovernmentScheme() {
    }

    public GovernmentScheme(
            String name,
            String marathi,
            String subtitle,
            String marathiSubtitle,
            String description,
            String marathiDescription,
            String benefit,
            String marathiBenefit,
            String coverage,
            String coverageLabel,
            String marathiCoverageLabel,
            String imageUrl,
            boolean active
    ) {
        this.name = name;
        this.marathi = marathi;
        this.subtitle = subtitle;
        this.marathiSubtitle = marathiSubtitle;
        this.description = description;
        this.marathiDescription = marathiDescription;
        this.benefit = benefit;
        this.marathiBenefit = marathiBenefit;
        this.coverage = coverage;
        this.coverageLabel = coverageLabel;
        this.marathiCoverageLabel = marathiCoverageLabel;
        this.imageUrl = imageUrl;
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


    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }


    public String getMarathiSubtitle() {
        return marathiSubtitle;
    }

    public void setMarathiSubtitle(String marathiSubtitle) {
        this.marathiSubtitle = marathiSubtitle;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public String getMarathiDescription() {
        return marathiDescription;
    }

    public void setMarathiDescription(String marathiDescription) {
        this.marathiDescription = marathiDescription;
    }


    public String getBenefit() {
        return benefit;
    }

    public void setBenefit(String benefit) {
        this.benefit = benefit;
    }


    public String getMarathiBenefit() {
        return marathiBenefit;
    }

    public void setMarathiBenefit(String marathiBenefit) {
        this.marathiBenefit = marathiBenefit;
    }


    public String getCoverage() {
        return coverage;
    }

    public void setCoverage(String coverage) {
        this.coverage = coverage;
    }


    public String getCoverageLabel() {
        return coverageLabel;
    }

    public void setCoverageLabel(String coverageLabel) {
        this.coverageLabel = coverageLabel;
    }


    public String getMarathiCoverageLabel() {
        return marathiCoverageLabel;
    }

    public void setMarathiCoverageLabel(String marathiCoverageLabel) {
        this.marathiCoverageLabel = marathiCoverageLabel;
    }


    // =====================================================
    // IMAGE GETTER / SETTER
    // =====================================================

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }


    // =====================================================
    // ACTIVE GETTER / SETTER
    // =====================================================

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}