package com.data.spark.service.reader;

import com.data.spark.CsvStorage;
import com.data.spark.Storage;
import lombok.AllArgsConstructor;
import org.apache.spark.sql.SparkSession;

@AllArgsConstructor
public class CsvReader implements InputReader {
    private SparkSession sparkSession;
    @Override
    public void readInput(Storage input) {
        var csvInput = (CsvStorage) input;
        sparkSession.read()
                .options(csvInput.getOptions())
                .csv(csvInput.getAbsolutePath())
                .createOrReplaceTempView(csvInput.getId());
    }

    public boolean isCompatible(Storage input){return input instanceof CsvStorage;}
}
