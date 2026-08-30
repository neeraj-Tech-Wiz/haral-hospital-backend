package com.haral.hospital_backend.service;

import com.haral.hospital_backend.entity.Patient;
import com.haral.hospital_backend.entity.PatientDocument;
import com.haral.hospital_backend.repository.PatientDocumentRepository;
import com.haral.hospital_backend.repository.PatientRepository;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class PatientDocumentService {

    private final PatientDocumentRepository documentRepository;

    private final PatientRepository patientRepository;

    private final SupabaseStorageService storageService;


    public PatientDocumentService(
            PatientDocumentRepository documentRepository,
            PatientRepository patientRepository,
            SupabaseStorageService storageService
    ) {

        this.documentRepository =
                documentRepository;

        this.patientRepository =
                patientRepository;

        this.storageService =
                storageService;
    }


    // =====================================================
    // UPLOAD DOCUMENT
    // =====================================================

    public PatientDocument uploadDocument(
            Long patientId,
            String documentType,
            MultipartFile file
    ) throws IOException, InterruptedException {

        Patient patient =
                patientRepository
                        .findById(patientId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Patient not found with id: "
                                                + patientId
                                )
                        );

        if (documentType == null ||
                documentType.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Document type is required"
            );
        }

        // Upload to private Supabase bucket
        String storagePath =
                storageService.uploadPatientDocument(
                        file,
                        patientId
                );

        PatientDocument document =
                new PatientDocument();

        document.setPatient(patient);

        document.setDocumentType(
                documentType.trim()
        );

        document.setFileName(
                file.getOriginalFilename()
        );

        // Store PATH, not public URL
        document.setFileUrl(
                storagePath
        );

        document.setFileSize(
                file.getSize()
        );

        document.setContentType(
                file.getContentType()
        );

        return documentRepository.save(
                document
        );
    }


    // =====================================================
    // GET PATIENT DOCUMENTS
    // =====================================================

    public List<PatientDocument> getPatientDocuments(
            Long patientId
    ) {

        if (!patientRepository.existsById(
                patientId
        )) {

            throw new RuntimeException(
                    "Patient not found with id: "
                            + patientId
            );
        }

        return documentRepository
                .findByPatientIdOrderByUploadedAtDesc(
                        patientId
                );
    }


    // =====================================================
    // GET SIGNED URL
    // =====================================================

    public String getSignedUrl(
            Long documentId
    ) throws IOException, InterruptedException {

        PatientDocument document =
                documentRepository
                        .findById(documentId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Patient document not found with id: "
                                                + documentId
                                )
                        );

        return storageService
                .createPatientDocumentSignedUrl(
                        document.getFileUrl()
                );
    }


    // =====================================================
    // DELETE DOCUMENT
    // =====================================================

    public void deleteDocument(
            Long documentId
    ) throws IOException, InterruptedException {

        PatientDocument document =
                documentRepository
                        .findById(documentId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Patient document not found with id: "
                                                + documentId
                                )
                        );

        // Delete actual file from Supabase
        storageService.deletePatientDocument(
                document.getFileUrl()
        );

        // Delete database record
        documentRepository.deleteById(
                documentId
        );
    }
}