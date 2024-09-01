package com.cousincodelabs.dynamo.controller;

import com.cousincodelabs.dynamo.dto.TableCreationRequest;
import com.cousincodelabs.dynamo.dto.TableModificationRequest;
import com.cousincodelabs.dynamo.service.DynamoDbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dynamodb")
public class DynamoDbController {

    private final DynamoDbService dynamoDbService;

    @Autowired
    public DynamoDbController(DynamoDbService dynamoDbService) {
        this.dynamoDbService = dynamoDbService;
    }

    @PostMapping("/createTable")
    public ResponseEntity<String> createTable(@RequestBody TableCreationRequest request) {
        try {
            dynamoDbService.createTable(request);
            return ResponseEntity.ok("Table created successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create table: " + e.getMessage());
        }
    }



    @PostMapping("/createSimpleTable")
    public ResponseEntity<String> createSimpleTable(@RequestBody String tableName) {
        if (!isValidTableName(tableName)) {
            return ResponseEntity.badRequest().body("Invalid table name. It must be between 3 and 255 characters long and can only contain a-z, A-Z, 0-9, '_', '-', and '.'.");
        }
        try {
            dynamoDbService.createSimpleTable(tableName);
            return ResponseEntity.ok("Table created successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create table: " + e.getMessage());
        }
    }

    private boolean isValidTableName(String tableName) {
        return tableName != null
                && tableName.length() >= 3
                && tableName.length() <= 255
                && tableName.matches("[a-zA-Z0-9_\\-.]+");
    }



    @PostMapping("/modifyTable")
    public ResponseEntity<String> modifyTable(@RequestBody TableModificationRequest request) {
        try {
            dynamoDbService.modifyTable(request);
            return ResponseEntity.ok("Table modified successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to modify table: " + e.getMessage());
        }
    }
}

