package com.data.spark;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.NoArgsConstructor;
import lombok.Value;

@NoArgsConstructor(force = true)
@Value
public class Target {
    @JsonProperty(value = "from_sql_query")
    private String fromSqlQuery;
    private Storage output;

    public String sql(){
        return "select * from " + fromSqlQuery;
    }
}
