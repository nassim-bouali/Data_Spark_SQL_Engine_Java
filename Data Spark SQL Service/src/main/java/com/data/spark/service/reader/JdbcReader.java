package com.data.spark.service.reader;

import com.data.spark.JdbcStorage;
import com.data.spark.Storage;
import com.data.spark.service.configuration.StorageUtils;
import lombok.AllArgsConstructor;
import org.apache.spark.sql.SparkSession;

import java.util.Map;
import java.util.Properties;

@AllArgsConstructor
public class JdbcReader implements InputReader {
    private SparkSession sparkSession;
    @Override
    public void readInput(Storage input) {
        var jdbcInput = (JdbcStorage) input;
        sparkSession.read()
                .options(jdbcInput.getOptions())
                .jdbc(jdbcInput.getUri(),
                        jdbcInput.getTable(),
                        StorageUtils.getProperties(jdbcInput.getOptions()))
                .createOrReplaceTempView(jdbcInput.getId());
    }

    public boolean isCompatible(Storage input){return input instanceof JdbcStorage;}
}
