package com.haral.hospital_backend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.UUID;

@Service
public class SupabaseStorageService {

    @Value("${supabase.url}")
    private String supabaseUrl;

    @Value("${supabase.service-key}")
    private String serviceKey;

    @Value("${supabase.storage.bucket}")
    private String bucket;

    private final HttpClient httpClient = HttpClient.newHttpClient();


    // =========================================================
    // DOCTOR IMAGE UPLOAD
    // =========================================================

    public String uploadDoctorImage(
            Long doctorId,
            MultipartFile file
    ) throws IOException, InterruptedException {

        validateImage(file);

        String contentType = file.getContentType();

        String extension = getExtension(contentType);

        String fileName =
                "doctor-"
                        + doctorId
                        + "-"
                        + UUID.randomUUID()
                        + extension;

        String uploadUrl =
                supabaseUrl
                        + "/storage/v1/object/"
                        + bucket
                        + "/"
                        + fileName;

        uploadToSupabase(
                uploadUrl,
                contentType,
                file
        );

        return supabaseUrl
                + "/storage/v1/object/public/"
                + bucket
                + "/"
                + fileName;
    }


    // =========================================================
    // FACILITY IMAGE UPLOAD
    // =========================================================

    public String uploadFacilityImage(
            MultipartFile file,
            Long facilityId
    ) throws IOException, InterruptedException {

        validateImage(file);

        String contentType = file.getContentType();

        String extension = getExtension(contentType);

        String fileName =
                "facility-"
                        + facilityId
                        + "-"
                        + UUID.randomUUID()
                        + extension;

        String facilityBucket = "facility-images";

        String uploadUrl =
                supabaseUrl
                        + "/storage/v1/object/"
                        + facilityBucket
                        + "/"
                        + fileName;

        uploadToSupabase(
                uploadUrl,
                contentType,
                file
        );

        return supabaseUrl
                + "/storage/v1/object/public/"
                + facilityBucket
                + "/"
                + fileName;
    }


    // =========================================================
    // COMMON SUPABASE UPLOAD
    // =========================================================

    private void uploadToSupabase(
            String uploadUrl,
            String contentType,
            MultipartFile file
    ) throws IOException, InterruptedException {

        HttpRequest request =
                HttpRequest.newBuilder()
                        .uri(URI.create(uploadUrl))

                        .header(
                                "Authorization",
                                "Bearer " + serviceKey
                        )

                        .header(
                                "apikey",
                                serviceKey
                        )

                        .header(
                                "Content-Type",
                                contentType
                        )

                        .header(
                                "x-upsert",
                                "true"
                        )

                        .POST(
                                HttpRequest.BodyPublishers.ofByteArray(
                                        file.getBytes()
                                )
                        )

                        .build();

        HttpResponse<String> response =
                httpClient.send(
                        request,
                        HttpResponse.BodyHandlers.ofString()
                );

        if (response.statusCode() < 200
                || response.statusCode() >= 300) {

            throw new RuntimeException(
                    "Supabase upload failed: "
                            + response.statusCode()
                            + " - "
                            + response.body()
            );
        }
    }


    // =========================================================
    // IMAGE VALIDATION
    // =========================================================

    private void validateImage(
            MultipartFile file
    ) {

        if (file == null || file.isEmpty()) {

            throw new IllegalArgumentException(
                    "Image file is required"
            );
        }

        String contentType =
                file.getContentType();

        if (contentType == null
                || !(
                contentType.equals("image/jpeg")
                        || contentType.equals("image/png")
                        || contentType.equals("image/webp")
        )) {

            throw new IllegalArgumentException(
                    "Only JPG, PNG and WEBP images are allowed"
            );
        }

        if (file.getSize() > 5 * 1024 * 1024) {

            throw new IllegalArgumentException(
                    "Image size must not exceed 5 MB"
            );
        }
    }


    // =========================================================
    // FILE EXTENSION
    // =========================================================

    private String getExtension(
            String contentType
    ) {

        return switch (contentType) {

            case "image/jpeg" -> ".jpg";

            case "image/png" -> ".png";

            case "image/webp" -> ".webp";

            default -> "";
        };
    }
    public String uploadHospitalImage(MultipartFile file)
            throws IOException, InterruptedException {

        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException(
                    "Hospital image is required"
            );
        }

        String contentType = file.getContentType();

        if (contentType == null ||
                (!contentType.equals("image/jpeg")
                        && !contentType.equals("image/png")
                        && !contentType.equals("image/webp"))) {

            throw new IllegalArgumentException(
                    "Only JPEG, PNG and WebP images are allowed"
            );
        }

        if (file.getSize() > 5 * 1024 * 1024) {
            throw new IllegalArgumentException(
                    "Image size must be less than 5 MB"
            );
        }

        String extension;

        if (contentType.equals("image/png")) {
            extension = ".png";
        } else if (contentType.equals("image/webp")) {
            extension = ".webp";
        } else {
            extension = ".jpg";
        }

        String fileName =
                "hospital-" +
                        UUID.randomUUID() +
                        extension;

        String hospitalBucket = "hospital-images";

        String uploadUrl =
                supabaseUrl +
                        "/storage/v1/object/" +
                        hospitalBucket +
                        "/" +
                        fileName;

        HttpRequest request =
                HttpRequest.newBuilder()
                        .uri(URI.create(uploadUrl))
                        .header(
                                "Authorization",
                                "Bearer " + serviceKey
                        )
                        .header(
                                "apikey",
                                serviceKey
                        )
                        .header(
                                "Content-Type",
                                contentType
                        )
                        .header(
                                "x-upsert",
                                "true"
                        )
                        .POST(
                                HttpRequest.BodyPublishers.ofByteArray(
                                        file.getBytes()
                                )
                        )
                        .build();

        HttpResponse<String> response =
                httpClient.send(
                        request,
                        HttpResponse.BodyHandlers.ofString()
                );

        if (response.statusCode() < 200 ||
                response.statusCode() >= 300) {

            throw new RuntimeException(
                    "Supabase hospital image upload failed: "
                            + response.statusCode()
                            + " - "
                            + response.body()
            );
        }

        return supabaseUrl
                + "/storage/v1/object/public/"
                + hospitalBucket
                + "/"
                + fileName;
    }
    public String uploadGovernmentSchemeImage(
            MultipartFile file,
            Long schemeId
    ) throws IOException, InterruptedException {

        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException(
                    "Government scheme image is required"
            );
        }

        String contentType = file.getContentType();

        if (contentType == null ||
                (!contentType.equals("image/jpeg")
                        && !contentType.equals("image/png")
                        && !contentType.equals("image/webp"))) {

            throw new IllegalArgumentException(
                    "Only JPEG, PNG and WebP images are allowed"
            );
        }

        if (file.getSize() > 5 * 1024 * 1024) {
            throw new IllegalArgumentException(
                    "Image size must be less than 5 MB"
            );
        }

        String extension;

        if (contentType.equals("image/png")) {
            extension = ".png";
        } else if (contentType.equals("image/webp")) {
            extension = ".webp";
        } else {
            extension = ".jpg";
        }

        String fileName =
                "government-scheme-" +
                        schemeId +
                        "-" +
                        UUID.randomUUID() +
                        extension;

        String bucket =
                "government-scheme-images";

        String uploadUrl =
                supabaseUrl +
                        "/storage/v1/object/" +
                        bucket +
                        "/" +
                        fileName;

        HttpRequest request =
                HttpRequest.newBuilder()
                        .uri(URI.create(uploadUrl))
                        .header(
                                "Authorization",
                                "Bearer " + serviceKey
                        )
                        .header(
                                "apikey",
                                serviceKey
                        )
                        .header(
                                "Content-Type",
                                contentType
                        )
                        .header(
                                "x-upsert",
                                "true"
                        )
                        .POST(
                                HttpRequest.BodyPublishers
                                        .ofByteArray(
                                                file.getBytes()
                                        )
                        )
                        .build();

        HttpResponse<String> response =
                httpClient.send(
                        request,
                        HttpResponse.BodyHandlers
                                .ofString()
                );

        if (response.statusCode() < 200 ||
                response.statusCode() >= 300) {

            throw new RuntimeException(
                    "Supabase government scheme image upload failed: "
                            + response.statusCode()
                            + " - "
                            + response.body()
            );
        }

        return supabaseUrl
                + "/storage/v1/object/public/"
                + bucket
                + "/"
                + fileName;
    }
    // =========================================================
// SPECIALITY IMAGE UPLOAD
// =========================================================

    public String uploadSpecialityImage(
            MultipartFile file,
            Long specialityId
    ) throws IOException, InterruptedException {

        if (file == null || file.isEmpty()) {

            throw new IllegalArgumentException(
                    "Speciality image is required"
            );
        }

        String contentType = file.getContentType();

        if (contentType == null ||
                (!contentType.equals("image/jpeg")
                        && !contentType.equals("image/png")
                        && !contentType.equals("image/webp"))) {

            throw new IllegalArgumentException(
                    "Only JPEG, PNG and WebP images are allowed"
            );
        }

        if (file.getSize() > 5 * 1024 * 1024) {

            throw new IllegalArgumentException(
                    "Image size must be less than 5 MB"
            );
        }

        String extension;

        if (contentType.equals("image/png")) {

            extension = ".png";

        } else if (contentType.equals("image/webp")) {

            extension = ".webp";

        } else {

            extension = ".jpg";
        }


        // ---------------------------------------------------------
        // UNIQUE FILE NAME
        // ---------------------------------------------------------

        String fileName =
                "speciality-" +
                        specialityId +
                        "-" +
                        UUID.randomUUID() +
                        extension;


        // ---------------------------------------------------------
        // SPECIALITY BUCKET
        // ---------------------------------------------------------

        String specialityBucket =
                "speciality-images";


        // ---------------------------------------------------------
        // SUPABASE UPLOAD URL
        // ---------------------------------------------------------

        String uploadUrl =
                supabaseUrl +
                        "/storage/v1/object/" +
                        specialityBucket +
                        "/" +
                        fileName;


        // ---------------------------------------------------------
        // UPLOAD
        // ---------------------------------------------------------

        HttpRequest request =
                HttpRequest.newBuilder()
                        .uri(URI.create(uploadUrl))

                        .header(
                                "Authorization",
                                "Bearer " + serviceKey
                        )

                        .header(
                                "apikey",
                                serviceKey
                        )

                        .header(
                                "Content-Type",
                                contentType
                        )

                        .header(
                                "x-upsert",
                                "true"
                        )

                        .POST(
                                HttpRequest.BodyPublishers
                                        .ofByteArray(
                                                file.getBytes()
                                        )
                        )

                        .build();


        HttpResponse<String> response =
                httpClient.send(
                        request,
                        HttpResponse.BodyHandlers
                                .ofString()
                );


        // ---------------------------------------------------------
        // CHECK RESPONSE
        // ---------------------------------------------------------

        if (response.statusCode() < 200 ||
                response.statusCode() >= 300) {

            throw new RuntimeException(
                    "Supabase speciality image upload failed: "
                            + response.statusCode()
                            + " - "
                            + response.body()
            );
        }


        // ---------------------------------------------------------
        // PUBLIC IMAGE URL
        // ---------------------------------------------------------

        return supabaseUrl
                + "/storage/v1/object/public/"
                + specialityBucket
                + "/"
                + fileName;
    }
    // =========================================================
// PATIENT DOCUMENT UPLOAD
// Private Supabase bucket
// =========================================================

    public String uploadPatientDocument(
            MultipartFile file,
            Long patientId
    ) throws IOException, InterruptedException {

        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException(
                    "Patient document is required"
            );
        }

        String contentType = file.getContentType();

        if (contentType == null) {
            throw new IllegalArgumentException(
                    "Unable to determine file type"
            );
        }

        // -----------------------------------------------------
        // ALLOWED FILE TYPES
        // -----------------------------------------------------

        boolean allowed =
                contentType.equals("application/pdf")
                        || contentType.equals("image/jpeg")
                        || contentType.equals("image/png")
                        || contentType.equals("image/webp")
                        || contentType.equals("application/msword")
                        || contentType.equals(
                        "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
                );

        if (!allowed) {
            throw new IllegalArgumentException(
                    "Only PDF, JPG, PNG, WEBP, DOC and DOCX files are allowed"
            );
        }

        // -----------------------------------------------------
        // MAX SIZE — 10 MB
        // -----------------------------------------------------

        if (file.getSize() > 10 * 1024 * 1024) {
            throw new IllegalArgumentException(
                    "Patient document size must be less than 10 MB"
            );
        }

        // -----------------------------------------------------
        // EXTENSION
        // -----------------------------------------------------

        String extension =
                getPatientDocumentExtension(contentType);

        // -----------------------------------------------------
        // UNIQUE STORAGE PATH
        // Organize files by patient
        // -----------------------------------------------------

        String fileName =
                "patient-"
                        + patientId
                        + "-"
                        + UUID.randomUUID()
                        + extension;

        String storagePath =
                "patient-" + patientId + "/" + fileName;

        String bucket =
                "patient-documents";

        // -----------------------------------------------------
        // SUPABASE UPLOAD URL
        // -----------------------------------------------------

        String uploadUrl =
                supabaseUrl
                        + "/storage/v1/object/"
                        + bucket
                        + "/"
                        + storagePath;

        HttpRequest request =
                HttpRequest.newBuilder()
                        .uri(URI.create(uploadUrl))
                        .header(
                                "Authorization",
                                "Bearer " + serviceKey
                        )
                        .header(
                                "apikey",
                                serviceKey
                        )
                        .header(
                                "Content-Type",
                                contentType
                        )
                        .header(
                                "x-upsert",
                                "false"
                        )
                        .POST(
                                HttpRequest.BodyPublishers
                                        .ofByteArray(
                                                file.getBytes()
                                        )
                        )
                        .build();

        HttpResponse<String> response =
                httpClient.send(
                        request,
                        HttpResponse.BodyHandlers.ofString()
                );

        if (response.statusCode() < 200 ||
                response.statusCode() >= 300) {

            throw new RuntimeException(
                    "Patient document upload failed: "
                            + response.statusCode()
                            + " - "
                            + response.body()
            );
        }

        // IMPORTANT:
        // Return storage PATH, NOT public URL.
        return storagePath;
    }
    // =========================================================
// GENERATE SIGNED URL
// Valid temporarily for viewing/downloading
// =========================================================
// =========================================================
// GENERATE SIGNED URL
// =========================================================

    public String createPatientDocumentSignedUrl(
            String storagePath
    ) throws IOException, InterruptedException {

        String bucket =
                "patient-documents";

        String signUrl =
                supabaseUrl
                        + "/storage/v1/object/sign/"
                        + bucket
                        + "/"
                        + storagePath;

        String body =
                "{\"expiresIn\":3600}";

        HttpRequest request =
                HttpRequest.newBuilder()
                        .uri(URI.create(signUrl))
                        .header(
                                "Authorization",
                                "Bearer " + serviceKey
                        )
                        .header(
                                "apikey",
                                serviceKey
                        )
                        .header(
                                "Content-Type",
                                "application/json"
                        )
                        .POST(
                                HttpRequest.BodyPublishers
                                        .ofString(body)
                        )
                        .build();

        HttpResponse<String> response =
                httpClient.send(
                        request,
                        HttpResponse.BodyHandlers.ofString()
                );

        if (response.statusCode() < 200 ||
                response.statusCode() >= 300) {

            throw new RuntimeException(
                    "Failed to generate signed URL: "
                            + response.statusCode()
                            + " - "
                            + response.body()
            );
        }

        ObjectMapper objectMapper =
                new ObjectMapper();

        JsonNode json =
                objectMapper.readTree(
                        response.body()
                );

        String signedUrl =
                json.get("signedURL").asText();


        // =====================================================
        // FIX SUPABASE SIGNED URL PATH
        // =====================================================

        if (signedUrl.startsWith("/object/sign/")) {

            signedUrl =
                    supabaseUrl
                            + "/storage/v1"
                            + signedUrl;

        } else if (signedUrl.startsWith("/storage/v1/object/sign/")) {

            signedUrl =
                    supabaseUrl
                            + signedUrl;

        } else if (!signedUrl.startsWith("http")) {

            signedUrl =
                    supabaseUrl
                            + "/storage/v1"
                            + (
                            signedUrl.startsWith("/")
                                    ? signedUrl
                                    : "/" + signedUrl
                    );
        }


        return signedUrl;
    }
    // =========================================================
// DELETE PATIENT DOCUMENT
// =========================================================

    public void deletePatientDocument(
            String storagePath
    ) throws IOException, InterruptedException {

        String bucket =
                "patient-documents";

        String deleteUrl =
                supabaseUrl
                        + "/storage/v1/object/"
                        + bucket
                        + "/"
                        + storagePath;

        HttpRequest request =
                HttpRequest.newBuilder()
                        .uri(URI.create(deleteUrl))
                        .header(
                                "Authorization",
                                "Bearer " + serviceKey
                        )
                        .header(
                                "apikey",
                                serviceKey
                        )
                        .DELETE()
                        .build();

        HttpResponse<String> response =
                httpClient.send(
                        request,
                        HttpResponse.BodyHandlers.ofString()
                );

        if (response.statusCode() < 200 ||
                response.statusCode() >= 300) {

            throw new RuntimeException(
                    "Patient document deletion failed: "
                            + response.statusCode()
                            + " - "
                            + response.body()
            );
        }
    }
    // =========================================================
// PATIENT DOCUMENT EXTENSION
// =========================================================

    private String getPatientDocumentExtension(
            String contentType
    ) {

        return switch (contentType) {

            case "application/pdf" ->
                    ".pdf";

            case "image/jpeg" ->
                    ".jpg";

            case "image/png" ->
                    ".png";

            case "image/webp" ->
                    ".webp";

            case "application/msword" ->
                    ".doc";

            case "application/vnd.openxmlformats-officedocument.wordprocessingml.document" ->
                    ".docx";

            default ->
                    "";
        };
    }
}