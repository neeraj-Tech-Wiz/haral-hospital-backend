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

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // ===============================
    // CORS CONFIGURATION
    // ===============================

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

    // ===============================
    // SECURITY FILTER CHAIN
    // ===============================

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http
    ) throws Exception {

        http

                // CORS
                .cors(cors ->
                        cors.configurationSource(
                                corsConfigurationSource()
                        )
                )

                // CSRF disabled for JWT REST API
                .csrf(csrf ->
                        csrf.disable()
                )

                // JWT = stateless
                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )

                // ===============================
                // AUTHORIZATION
                // ===============================

                .authorizeHttpRequests(auth -> auth

                        // CORS preflight
                        .requestMatchers(
                                HttpMethod.OPTIONS,
                                "/**"
                        ).permitAll()

                        // Admin login
                        .requestMatchers(
                                "/api/admin/login"
                        ).permitAll()
                        .requestMatchers("/api/health").permitAll()

                        // Patient creates appointment
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/appointments"
                        ).permitAll()

                        // Admin reads appointments
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/appointments"
                        ).authenticated()

                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/appointments/**"
                        ).authenticated()

                        // Admin updates appointments
                        .requestMatchers(
                                HttpMethod.PUT,
                                "/api/appointments/**"
                        ).authenticated()

                        // Admin deletes appointments
                        .requestMatchers(
                                HttpMethod.DELETE,
                                "/api/appointments/**"
                        ).authenticated()

                        // Everything else
                        .anyRequest().authenticated()
                )

                // Disable browser login
                .formLogin(form ->
                        form.disable()
                )

                // Disable HTTP Basic
                .httpBasic(basic ->
                        basic.disable()
                )

                // JWT filter
                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }
}