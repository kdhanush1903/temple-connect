package com.cousincodelabs.dynamo.dto;

import java.util.List;

public class TableCreationRequest {
    private String tableName;
    private String keyAttributeName;
    private List<AttributeDefinitionDto> attributes;
    private List<KeySchemaElementDto> keySchema;
    private ProvisionedThroughputDto provisionedThroughput;

    // Getters and Setters
    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getKeyAttributeName() {
        return keyAttributeName;
    }

    public void setKeyAttributeName(String keyAttributeName) {
        this.keyAttributeName = keyAttributeName;
    }

    public List<AttributeDefinitionDto> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<AttributeDefinitionDto> attributes) {
        this.attributes = attributes;
    }

    public List<KeySchemaElementDto> getKeySchema() {
        return keySchema;
    }

    public void setKeySchema(List<KeySchemaElementDto> keySchema) {
        this.keySchema = keySchema;
    }

    public ProvisionedThroughputDto getProvisionedThroughput() {
        return provisionedThroughput;
    }

    public void setProvisionedThroughput(ProvisionedThroughputDto provisionedThroughput) {
        this.provisionedThroughput = provisionedThroughput;
    }

    public static class AttributeDefinitionDto {
        private String attributeName;
        private String attributeType;

        // Getters and Setters
        public String getAttributeName() {
            return attributeName;
        }

        public void setAttributeName(String attributeName) {
            this.attributeName = attributeName;
        }

        public String getAttributeType() {
            return attributeType;
        }

        public void setAttributeType(String attributeType) {
            this.attributeType = attributeType;
        }
    }

    public static class KeySchemaElementDto {
        private String attributeName;
        private String keyType;

        // Getters and Setters
        public String getAttributeName() {
            return attributeName;
        }

        public void setAttributeName(String attributeName) {
            this.attributeName = attributeName;
        }

        public String getKeyType() {
            return keyType;
        }

        public void setKeyType(String keyType) {
            this.keyType = keyType;
        }
    }

    public static class ProvisionedThroughputDto {
        private Long readCapacityUnits;
        private Long writeCapacityUnits;

        // Getters and Setters
        public Long getReadCapacityUnits() {
            return readCapacityUnits;
        }

        public void setReadCapacityUnits(Long readCapacityUnits) {
            this.readCapacityUnits = readCapacityUnits;
        }

        public Long getWriteCapacityUnits() {
            return writeCapacityUnits;
        }

        public void setWriteCapacityUnits(Long writeCapacityUnits) {
            this.writeCapacityUnits = writeCapacityUnits;
        }
    }
}
