package com.data.spark;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;


@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = CsvStorage.class, name = "csv"),
        @JsonSubTypes.Type(value = ParquetStorage.class, name = "parquet"),
        @JsonSubTypes.Type(value = JdbcStorage.class, name = "jdbc")
})public abstract class Storage {
    public abstract String getId();
    public abstract String getPath();
    public abstract String getStorageAccount();
    public abstract String getContainer();
    public abstract String getSasToken();

    public String getAbsolutePath() {
        return getStorageAccount() == null ? getPath() : "abfss://" + getContainer() + "@" + getStorageAccount() + ".dfs.core.windows.net/"+ getPath();
    }
}
