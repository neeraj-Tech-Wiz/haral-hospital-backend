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

        this.jwtAuthenticationFilter =
                jwtAuthenticationFilter;
    }

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
                // STATELESS JWT SESSION
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


                        // -----------------------------------------
                        // PUBLIC ENDPOINTS
                        // -----------------------------------------

                        // Admin login
                        .requestMatchers(
                                "/api/admin/login"
                        ).permitAll()

                        // Health check
                        .requestMatchers(
                                "/api/health"
                        ).permitAll()


                        // -----------------------------------------
                        // PATIENT APPOINTMENT
                        // -----------------------------------------

                        // Patient submits final appointment
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/appointments"
                        ).permitAll()


                        // -----------------------------------------
                        // PATIENT APPOINTMENT DRAFT
                        // -----------------------------------------

                        // Create new draft
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/appointment-drafts"
                        ).permitAll()

                        // Update existing draft
                        //
                        // Example:
                        // PUT /api/appointment-drafts/29
                        //
                        .requestMatchers(
                                HttpMethod.PUT,
                                "/api/appointment-drafts/*"
                        ).permitAll()

                        // Mark draft as submitted
                        //
                        // Example:
                        // PUT /api/appointment-drafts/29/submitted
                        //
                        .requestMatchers(
                                HttpMethod.PUT,
                                "/api/appointment-drafts/*/submitted"
                        ).permitAll()


                        // -----------------------------------------
                        // ADMIN APPOINTMENT DRAFTS
                        // -----------------------------------------

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

                        // Admin endpoint, if your frontend uses it
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/appointment-drafts/admin"
                        ).authenticated()


                        // -----------------------------------------
                        // ADMIN APPOINTMENTS
                        // -----------------------------------------

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

                        // Update appointment status
                        .requestMatchers(
                                HttpMethod.PUT,
                                "/api/appointments/**"
                        ).authenticated()

                        // Delete appointment
                        .requestMatchers(
                                HttpMethod.DELETE,
                                "/api/appointments/**"
                        ).authenticated()


                        // -----------------------------------------
                        // EVERYTHING ELSE
                        // -----------------------------------------

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