package com.cousincodelabs.dynamo.service;

import com.cousincodelabs.dynamo.dto.TableCreationRequest;
import com.cousincodelabs.dynamo.dto.TableModificationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DynamoDbService {

    private final DynamoDbClient dynamoDbClient;

    @Autowired
    public DynamoDbService(DynamoDbClient dynamoDbClient) {
        this.dynamoDbClient = dynamoDbClient;
    }

    public void createTable(TableCreationRequest request) {
        // Build the Key Schema
        List<KeySchemaElement> keySchema = new ArrayList<>();
        for (TableCreationRequest.KeySchemaElementDto keySchemaElementDto : request.getKeySchema()) {
            KeySchemaElement element = KeySchemaElement.builder()
                    .attributeName(keySchemaElementDto.getAttributeName())
                    .keyType(KeyType.fromValue(keySchemaElementDto.getKeyType()))
                    .build();
            keySchema.add(element);
            System.out.println("Key Schema Element: " + element);
        }

        // Build the Attribute Definitions (Only for 'id')
        List<AttributeDefinition> attributeDefinitions = new ArrayList<>();
        for (TableCreationRequest.AttributeDefinitionDto attributeDto : request.getAttributes()) {
            // Include only the 'id' attribute in the attribute definitions
            if ("id".equals(attributeDto.getAttributeName())) {
                AttributeDefinition definition = AttributeDefinition.builder()
                        .attributeName(attributeDto.getAttributeName())
                        .attributeType(ScalarAttributeType.fromValue(attributeDto.getAttributeType()))
                        .build();
                attributeDefinitions.add(definition);
                System.out.println("Attribute Definition: " + definition);
            }
        }

        // Ensure all attributes in key schema are defined in attribute definitions
        List<String> keySchemaAttributes = keySchema.stream()
                .map(KeySchemaElement::attributeName)
                .collect(Collectors.toList());
        List<String> attributeDefinitionAttributes = attributeDefinitions.stream()
                .map(AttributeDefinition::attributeName)
                .collect(Collectors.toList());

        System.out.println("Key Schema Attributes: " + keySchemaAttributes);
        System.out.println("Attribute Definition Attributes: " + attributeDefinitionAttributes);

        if (!attributeDefinitionAttributes.containsAll(keySchemaAttributes)) {
            throw new IllegalArgumentException("All key schema attributes must be defined in attribute definitions.");
        }

        // Build CreateTableRequest
        CreateTableRequest createTableRequest = CreateTableRequest.builder()
                .tableName(request.getTableName())
                .keySchema(keySchema)
                .attributeDefinitions(attributeDefinitions)
                .provisionedThroughput(ProvisionedThroughput.builder()
                        .readCapacityUnits(request.getProvisionedThroughput().getReadCapacityUnits())
                        .writeCapacityUnits(request.getProvisionedThroughput().getWriteCapacityUnits())
                        .build())
                .build();

        System.out.println("CreateTableRequest: " + createTableRequest);

        dynamoDbClient.createTable(createTableRequest);
    }







    public void modifyTable(TableModificationRequest request) {
        // Retrieve the table schema
        DescribeTableRequest describeTableRequest = DescribeTableRequest.builder()
                .tableName(request.getTableName())
                .build();

        DescribeTableResponse describeTableResponse = dynamoDbClient.describeTable(describeTableRequest);
        List<AttributeDefinition> attributeDefinitions = new ArrayList<>(describeTableResponse.table().attributeDefinitions());

        // Add new attributes
        for (Map.Entry<String, String> entry : request.getNewAttributes().entrySet()) {
            boolean attributeExists = attributeDefinitions.stream()
                    .anyMatch(ad -> ad.attributeName().equals(entry.getKey()));

            if (!attributeExists) {
                attributeDefinitions.add(AttributeDefinition.builder()
                        .attributeName(entry.getKey())
                        .attributeType(ScalarAttributeType.fromValue(entry.getValue()))
                        .build());
            }
        }
    }




    public void createSimpleTable(String tableName) {
        List<KeySchemaElement> keySchema = List.of(
                KeySchemaElement.builder()
                        .attributeName("id")
                        .keyType(KeyType.HASH)
                        .build()
        );

        List<AttributeDefinition> attributeDefinitions = List.of(
                AttributeDefinition.builder()
                        .attributeName("id")
                        .attributeType(ScalarAttributeType.S)
                        .build()
        );

        CreateTableRequest createTableRequest = CreateTableRequest.builder()
                .tableName(tableName)
                .keySchema(keySchema)
                .attributeDefinitions(attributeDefinitions)
                .provisionedThroughput(ProvisionedThroughput.builder()
                        .readCapacityUnits(5L)
                        .writeCapacityUnits(5L)
                        .build())
                .build();

        dynamoDbClient.createTable(createTableRequest);
    }

}


