package com.cousincodelabs.admin.repository;

import com.cousincodelabs.admin.entity.Admin;
import org.springframework.stereotype.Repository;

import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;

import java.util.Optional;

@Repository
public class AdminRepository {

    private final DynamoDbTable<Admin> adminTable;

    public AdminRepository(DynamoDbEnhancedClient dynamoDbEnhancedClient) {
        this.adminTable = dynamoDbEnhancedClient.table("Admin", TableSchema.fromBean(Admin.class));
    }

    public Optional<Admin> findByUsername(String username) {
        try {
            return adminTable.scan()
                    .items()
                    .stream()
                    .filter(admin -> admin.getUsername().equals(username))
                    .findFirst();
        } catch (DynamoDbException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public Admin save(Admin admin) {
        try {
            adminTable.putItem(admin);
            return admin;
        } catch (DynamoDbException e) {
            e.printStackTrace();
            return null;
        }
    }
}

