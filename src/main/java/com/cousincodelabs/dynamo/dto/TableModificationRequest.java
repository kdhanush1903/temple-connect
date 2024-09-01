package com.cousincodelabs.dynamo.dto;

import java.util.List;
import java.util.Map;

public class TableModificationRequest {
    private String tableName;
    private Map<String, String> newAttributes; // Attributes to add or modify
    private List<String> attributesToRemove;   // Attributes to remove

    // Getters and Setters
    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public Map<String, String> getNewAttributes() {
        return newAttributes;
    }

    public void setNewAttributes(Map<String, String> newAttributes) {
        this.newAttributes = newAttributes;
    }

    public List<String> getAttributesToRemove() {
        return attributesToRemove;
    }

    public void setAttributesToRemove(List<String> attributesToRemove) {
        this.attributesToRemove = attributesToRemove;
    }
}

