package com.cousincodelabs.admin.controller;

import com.cousincodelabs.admin.dto.AdminResponse;
import com.cousincodelabs.admin.dto.LoginRequest;
import com.cousincodelabs.admin.service.AdminService;
import com.cousincodelabs.auth.AuthService;
import com.cousincodelabs.auth.JwtResponse;
import com.cousincodelabs.exception.ErrorResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.cousincodelabs.admin.dto.AdminRequest;


@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> registerAdmin(@RequestBody AdminRequest adminRequest) {
        try {
            AdminResponse response = adminService.register(adminRequest);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> loginAdmin(@RequestBody LoginRequest loginRequest) {
        JwtResponse response = authService.login(loginRequest);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
