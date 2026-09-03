package com.haral.hospital_backend.service;

import com.haral.hospital_backend.entity.Appointment;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class WhatsAppService {

    @Value("${whatsapp.api.url:http://localhost:8090}")
    private String apiUrl;

    @Value("${whatsapp.instance:haral-hospital}")
    private String instance;

    @Value("${whatsapp.api.key:}")
    private String apiKey;

    private final RestTemplate restTemplate;

    public WhatsAppService() {
        this.restTemplate = new RestTemplate();
    }

    public void sendAppointmentConfirmation(Appointment appointment) {

        try {

            // Check API key
            if (apiKey == null || apiKey.trim().isEmpty()) {
                System.err.println(
                        "WhatsApp API key is not configured. Skipping notification."
                );
                return;
            }

            // Format patient phone number
            String formattedPhone =
                    formatPhoneNumber(appointment.getPhone());

            if (formattedPhone.isEmpty()) {
                System.err.println(
                        "Invalid or empty patient phone number. Skipping WhatsApp notification."
                );
                return;
            }

            // Create WhatsApp message
            String messageText = String.format(
                    """
                    Dear %s,

                    Your appointment at Haral Hospital has been confirmed.

                    Doctor: %s
                    Date: %s
                    Time: %s

                    Thank you,
                    Haral Hospital
                    """,

                    appointment.getPatientName(),

                    appointment.getDoctor() != null
                            ? appointment.getDoctor()
                            : "Assigned Doctor",

                    appointment.getAppointmentDate(),

                    appointment.getAppointmentTime()
            );

            // Evolution API endpoint
            String endpoint =
                    apiUrl + "/message/sendText/" + instance;

            // Request headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("apikey", apiKey);

            // Request body
            Map<String, String> body = new HashMap<>();

            body.put("number", formattedPhone);
            body.put("text", messageText);

            // Create HTTP request
            HttpEntity<Map<String, String>> requestEntity =
                    new HttpEntity<>(body, headers);

            // Send request to Evolution API
            ResponseEntity<String> response =
                    restTemplate.postForEntity(
                            endpoint,
                            requestEntity,
                            String.class
                    );

            // Log result
            if (response.getStatusCode().is2xxSuccessful()) {

                System.out.println(
                        "WhatsApp appointment confirmation sent successfully to "
                                + formattedPhone
                );

            } else {

                System.err.println(
                        "Failed to send WhatsApp notification. Status: "
                                + response.getStatusCode()
                );
            }

        } catch (Exception e) {

            // WhatsApp failure should NOT affect appointment confirmation
            System.err.println(
                    "Error while sending WhatsApp notification: "
                            + e.getMessage()
            );
        }
    }


    // =====================================================
    // FORMAT PHONE NUMBER
    // =====================================================

    private String formatPhoneNumber(String phone) {

        if (phone == null || phone.trim().isEmpty()) {
            return "";
        }

        // Remove spaces, hyphens, brackets and plus sign
        String cleanNumber =
                phone.replaceAll("[\\s\\-\\+\\(\\)]", "");

        // If Indian mobile number has only 10 digits,
        // automatically add country code 91
        if (cleanNumber.length() == 10) {
            cleanNumber = "91" + cleanNumber;
        }

        return cleanNumber;
    }
}