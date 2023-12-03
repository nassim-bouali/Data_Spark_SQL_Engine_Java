package com.data.spark;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.HashMap;

@Getter
public class ParquetStorage extends Storage{
    private String id;
    private String path;
    @JsonProperty(value = "storage_account")
    private String storageAccount;
    private String container;
    @JsonProperty(value = "sas_token")
    private String sasToken;
    private HashMap<String, String> options;
}
