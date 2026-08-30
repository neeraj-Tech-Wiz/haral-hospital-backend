package com.haral.hospital_backend.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "patient_documents")
public class PatientDocument {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    // =====================================================
    // PATIENT
    // =====================================================

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "patient_id",
            nullable = false
    )
    private Patient patient;


    // =====================================================
    // DOCUMENT INFORMATION
    // =====================================================

    @Column(
            name = "document_type",
            nullable = false
    )
    private String documentType;


    @Column(
            name = "file_name",
            nullable = false
    )
    private String fileName;


    @Column(
            name = "file_url",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String fileUrl;


    @Column(name = "file_size")
    private Long fileSize;


    @Column(name = "content_type")
    private String contentType;


    // =====================================================
    // UPLOAD TIME
    // =====================================================

    @Column(
            name = "uploaded_at",
            nullable = false,
            updatable = false
    )
    private LocalDateTime uploadedAt;


    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public PatientDocument() {
    }


    // =====================================================
    // PRE-PERSIST
    // =====================================================

    @PrePersist
    protected void onCreate() {

        uploadedAt =
                LocalDateTime.now();
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


    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }


    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(
            String documentType) {

        this.documentType =
                documentType;
    }


    public String getFileName() {
        return fileName;
    }

    public void setFileName(
            String fileName) {

        this.fileName =
                fileName;
    }


    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(
            String fileUrl) {

        this.fileUrl =
                fileUrl;
    }


    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(
            Long fileSize) {

        this.fileSize =
                fileSize;
    }


    public String getContentType() {
        return contentType;
    }

    public void setContentType(
            String contentType) {

        this.contentType =
                contentType;
    }


    public LocalDateTime getUploadedAt() {
        return uploadedAt;
    }

    public void setUploadedAt(
            LocalDateTime uploadedAt) {

        this.uploadedAt =
                uploadedAt;
    }
}