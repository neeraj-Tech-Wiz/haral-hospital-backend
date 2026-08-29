package com.haral.hospital_backend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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
}