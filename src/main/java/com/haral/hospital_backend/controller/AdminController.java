package com.haral.hospital_backend.controller;

import com.haral.hospital_backend.dto.AdminLoginRequest;
import com.haral.hospital_backend.entity.Admin;
import com.haral.hospital_backend.security.JwtService;
import com.haral.hospital_backend.service.AdminService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "http://localhost:5173")
public class AdminController {

    private final AdminService adminService;
    private final JwtService jwtService;

    public AdminController(
            AdminService adminService,
            JwtService jwtService) {

        this.adminService = adminService;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody AdminLoginRequest request) {

        Admin admin =
                adminService.findByUsername(request.getUsername());

        if (admin == null) {

            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of(
                            "success", false,
                            "message", "Invalid username or password"
                    ));
        }

        boolean passwordMatches =
                adminService.verifyPassword(
                        request.getPassword(),
                        admin.getPassword()
                );

        if (!passwordMatches) {

            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of(
                            "success", false,
                            "message", "Invalid username or password"
                    ));
        }

        String token =
                jwtService.generateToken(admin.getUsername());

        return ResponseEntity.ok(
                Map.of(
                        "success", true,
                        "message", "Login successful",
                        "token", token,
                        "username", admin.getUsername()
                )
        );
    }

}