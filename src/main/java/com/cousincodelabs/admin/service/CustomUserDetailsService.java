package com.cousincodelabs.admin.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.util.HashMap;
import java.util.Map;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final DynamoDbClient dynamoDbClient;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public CustomUserDetailsService(DynamoDbClient dynamoDbClient, @Lazy PasswordEncoder passwordEncoder) {
        this.dynamoDbClient = dynamoDbClient;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("Fetching user: " + username);

        // Step 1: Scan to find the user by username
        Map<String, AttributeValue> expressionAttributeValues = new HashMap<>();
        expressionAttributeValues.put(":username", AttributeValue.builder().s(username).build());

        ScanRequest scanRequest = ScanRequest.builder()
                .tableName("Admin")
                .filterExpression("username = :username")
                .expressionAttributeValues(expressionAttributeValues)
                .build();

        try {
            ScanResponse scanResponse = dynamoDbClient.scan(scanRequest);
            Map<String, AttributeValue> item = scanResponse.items().stream().findFirst().orElse(null);

            if (item == null || item.isEmpty()) {
                throw new UsernameNotFoundException("User not found");
            }

            String id = item.get("id").s();
            String password = item.get("password").s();

            return User.withUsername(username)
                    .password(password) // Ensure you use the correct password format
                    .roles("ADMIN")
                    .build();
        } catch (Exception e) {
            // Log detailed error information
            System.err.println("Error retrieving user from DynamoDB: " + e.getMessage());
            throw new UsernameNotFoundException("User not found");
        }
    }}


