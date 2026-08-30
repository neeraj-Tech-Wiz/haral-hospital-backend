package com.haral.hospital_backend.controller;

import com.haral.hospital_backend.entity.PatientDocument;
import com.haral.hospital_backend.service.PatientDocumentService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/patients")
public class PatientDocumentController {

    private final PatientDocumentService documentService;


    public PatientDocumentController(
            PatientDocumentService documentService
    ) {

        this.documentService =
                documentService;
    }


    // =====================================================
    // UPLOAD DOCUMENT
    // =====================================================

    @PostMapping(
            value = "/{patientId}/documents",
            consumes = "multipart/form-data"
    )
    public ResponseEntity<?> uploadDocument(

            @PathVariable Long patientId,

            @RequestParam("documentType")
            String documentType,

            @RequestParam("file")
            MultipartFile file

    ) {

        try {

            PatientDocument document =
                    documentService.uploadDocument(
                            patientId,
                            documentType,
                            file
                    );

            return ResponseEntity.ok(
                    Map.of(
                            "success", true,
                            "message",
                            "Patient document uploaded successfully",
                            "document",
                            document
                    )
            );

        } catch (Exception e) {

            return ResponseEntity
                    .badRequest()
                    .body(
                            Map.of(
                                    "success", false,
                                    "message",
                                    e.getMessage()
                            )
                    );
        }
    }


    // =====================================================
    // GET PATIENT DOCUMENTS
    // =====================================================

    @GetMapping("/{patientId}/documents")
    public ResponseEntity<List<PatientDocument>>
    getPatientDocuments(
            @PathVariable Long patientId
    ) {

        return ResponseEntity.ok(
                documentService
                        .getPatientDocuments(
                                patientId
                        )
        );
    }


    // =====================================================
    // GET SIGNED VIEW / DOWNLOAD URL
    // =====================================================

    @GetMapping(
            "/documents/{documentId}/url"
    )
    public ResponseEntity<?> getDocumentUrl(
            @PathVariable Long documentId
    ) {

        try {

            String signedUrl =
                    documentService
                            .getSignedUrl(
                                    documentId
                            );

            return ResponseEntity.ok(
                    Map.of(
                            "success", true,
                            "url", signedUrl,
                            "expiresIn",
                            3600
                    )
            );

        } catch (Exception e) {

            return ResponseEntity
                    .badRequest()
                    .body(
                            Map.of(
                                    "success", false,
                                    "message",
                                    e.getMessage()
                            )
                    );
        }
    }


    // =====================================================
    // DELETE DOCUMENT
    // =====================================================

    @DeleteMapping(
            "/documents/{documentId}"
    )
    public ResponseEntity<?> deleteDocument(
            @PathVariable Long documentId
    ) {

        try {

            documentService.deleteDocument(
                    documentId
            );

            return ResponseEntity.ok(
                    Map.of(
                            "success", true,
                            "message",
                            "Patient document deleted successfully"
                    )
            );

        } catch (Exception e) {

            return ResponseEntity
                    .badRequest()
                    .body(
                            Map.of(
                                    "success", false,
                                    "message",
                                    e.getMessage()
                            )
                    );
        }
    }
}