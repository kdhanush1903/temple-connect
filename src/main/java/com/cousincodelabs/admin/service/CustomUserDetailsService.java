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
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest;
import software.amazon.awssdk.services.dynamodb.model.GetItemResponse;

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

        // Construct the key for DynamoDB using the 'id' attribute
        Map<String, AttributeValue> key = new HashMap<>();
        key.put("id", AttributeValue.builder().s(username).build());

        GetItemRequest getItemRequest = GetItemRequest.builder()
                .tableName("Admin")
                .key(key)
                .build();

        try {
            GetItemResponse response = dynamoDbClient.getItem(getItemRequest);
            Map<String, AttributeValue> item = response.item();

            if (item == null || item.isEmpty()) {
                throw new UsernameNotFoundException("User not found");
            }

            String password = item.get("password").s();
            return User.withUsername(username)
                    .password(password) // Use the password directly from DynamoDB
                    .roles("ADMIN")
                    .build();
        } catch (Exception e) {
            // Log detailed error information
            System.err.println("Error retrieving user from DynamoDB: " + e.getMessage());
            throw new UsernameNotFoundException("User not found");
        }
    }

}
