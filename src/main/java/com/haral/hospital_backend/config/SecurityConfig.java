package com.haral.hospital_backend.config;

import com.haral.hospital_backend.security.JwtAuthenticationFilter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(
            JwtAuthenticationFilter jwtAuthenticationFilter) {

        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    // =====================================================
    // PASSWORD ENCODER
    // =====================================================

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // =====================================================
    // CORS
    // =====================================================

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration =
                new CorsConfiguration();

        configuration.setAllowedOrigins(
                List.of(
                        "http://localhost:5173",
                        "https://haral-hospital-frontend.vercel.app"
                )
        );

        configuration.setAllowedMethods(
                List.of(
                        "GET",
                        "POST",
                        "PUT",
                        "PATCH",
                        "DELETE",
                        "OPTIONS"
                )
        );

        configuration.setAllowedHeaders(
                List.of(
                        "Authorization",
                        "Content-Type"
                )
        );

        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration(
                "/**",
                configuration
        );

        return source;
    }

    // =====================================================
    // SECURITY
    // =====================================================

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http
    ) throws Exception {

        http

                // =================================================
                // CORS
                // =================================================

                .cors(cors ->
                        cors.configurationSource(
                                corsConfigurationSource()
                        )
                )

                // =================================================
                // CSRF
                // =================================================

                .csrf(csrf ->
                        csrf.disable()
                )

                // =================================================
                // STATELESS JWT
                // =================================================

                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )

                // =================================================
                // AUTHORIZATION
                // =================================================

                .authorizeHttpRequests(auth -> auth

                        // -----------------------------------------
                        // CORS PRE-FLIGHT
                        // -----------------------------------------

                        .requestMatchers(
                                HttpMethod.OPTIONS,
                                "/**"
                        ).permitAll()


                        // =========================================
                        // PUBLIC ENDPOINTS
                        // =========================================

                        // Admin login
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/admin/login"
                        ).permitAll()

                        // Health check
                        .requestMatchers(
                                "/api/health"
                        ).permitAll()


                        // =========================================
                        // PUBLIC DOCTOR DATA
                        // =========================================

                        // Patients / website visitors can see doctors
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/doctors"
                        ).permitAll()

                        // If your public Doctors page requests
                        // a specific doctor by ID
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/doctors/*"
                        ).permitAll()


                        // =========================================
                        // PATIENT APPOINTMENT
                        // =========================================

                        // Patient submits appointment
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/appointments"
                        ).permitAll()


                        // =========================================
                        // PATIENT APPOINTMENT DRAFT
                        // =========================================

                        // Create draft
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/appointment-drafts"
                        ).permitAll()

                        // Update draft
                        .requestMatchers(
                                HttpMethod.PUT,
                                "/api/appointment-drafts/*"
                        ).permitAll()

                        // Mark draft as submitted
                        .requestMatchers(
                                HttpMethod.PUT,
                                "/api/appointment-drafts/*/submitted"
                        ).permitAll()


                        // =========================================
                        // ADMIN APPOINTMENT DRAFTS
                        // =========================================

                        // Currently filling
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/appointment-drafts/active"
                        ).authenticated()

                        // All draft history
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/appointment-drafts/all"
                        ).authenticated()

                        // Abandoned drafts
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/appointment-drafts/abandoned"
                        ).authenticated()

                        // Admin draft endpoint
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/appointment-drafts/admin"
                        ).authenticated()


                        // =========================================
                        // ADMIN APPOINTMENTS
                        // =========================================

                        // Get all appointments
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/appointments"
                        ).authenticated()

                        // Get appointment by ID
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/appointments/**"
                        ).authenticated()

                        // Update appointment
                        .requestMatchers(
                                HttpMethod.PUT,
                                "/api/appointments/**"
                        ).authenticated()

                        // Delete appointment
                        .requestMatchers(
                                HttpMethod.DELETE,
                                "/api/appointments/**"
                        ).authenticated()


                        // =========================================
                        // ADMIN DOCTOR MANAGEMENT
                        // =========================================

                        // Create doctor
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/doctors"
                        ).authenticated()

                        // Update doctor
                        .requestMatchers(
                                HttpMethod.PUT,
                                "/api/doctors/**"
                        ).authenticated()

                        // Delete doctor
                        .requestMatchers(
                                HttpMethod.DELETE,
                                "/api/doctors/**"
                        ).authenticated()

                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/hospital-info"
                        ).permitAll()

                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/facilities"
                        ).permitAll()


                        // Upload / replace doctor image
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/doctors/*/image"
                        ).authenticated()

                        .requestMatchers(
                                HttpMethod.GET, "/api/insurance"
                        ).permitAll()

                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/government-schemes"
                        ).permitAll()


                        // =========================================
                        // EVERYTHING ELSE
                        // =========================================

                        .anyRequest().authenticated()
                )

                // =================================================
                // DISABLE FORM LOGIN
                // =================================================

                .formLogin(form ->
                        form.disable()
                )

                // =================================================
                // DISABLE HTTP BASIC
                // =================================================

                .httpBasic(basic ->
                        basic.disable()
                )

                // =================================================
                // JWT FILTER
                // =================================================

                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }
}