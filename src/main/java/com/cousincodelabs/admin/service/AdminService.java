package com.cousincodelabs.admin.service;

import com.cousincodelabs.admin.dto.AdminRequest;
import com.cousincodelabs.admin.dto.AdminResponse;
import com.cousincodelabs.admin.entity.Admin;
import com.cousincodelabs.admin.repository.AdminRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AdminService {
    private static final Logger logger = LoggerFactory.getLogger(AdminService.class);


    @Autowired
    private AdminRepository adminRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AdminService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }




    public AdminResponse register(AdminRequest adminRequest) {
        if (adminRepository.findByEmail(adminRequest.getEmail()).isPresent()) {
            logger.warn("Email already exists: {}", adminRequest.getEmail());
            throw new IllegalArgumentException("Email already exists");
        }

        Admin admin = new Admin();
        admin.setId(UUID.randomUUID().toString());
        admin.setUsername(adminRequest.getUsername());
        admin.setPassword(passwordEncoder.encode(adminRequest.getPassword()));
        admin.setEmail(adminRequest.getEmail());
        admin.setFirstName(adminRequest.getFirstName());
        admin.setLastName(adminRequest.getLastName());

        admin = adminRepository.save(admin);
        logger.info("Admin successfully registered: {}", admin.getEmail());

        return new AdminResponse(admin.getId(), admin.getUsername(), admin.getEmail(), admin.getFirstName(), admin.getLastName());
    }
}


