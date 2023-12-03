package com.data.spark;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.Map;

@Getter
public class JdbcStorage extends Storage {
    private String id;
    private String path;
    @JsonProperty(value = "storage_account")
    private String storageAccount;
    private String container;
    @JsonProperty(value = "sas_token")
    private String sasToken;
    private String database;
    private String schema;
    private String table;
    private String request;
    private String uri;
    private boolean cache;
    private Map<String, String> options;
}