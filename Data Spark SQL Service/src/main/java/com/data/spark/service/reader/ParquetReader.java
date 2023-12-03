package com.data.spark.service.reader;

import com.data.spark.ParquetStorage;
import com.data.spark.Storage;
import lombok.AllArgsConstructor;
import org.apache.spark.sql.SparkSession;

@AllArgsConstructor
public class ParquetReader implements InputReader {
    private SparkSession sparkSession;
    @Override
    public void readInput(Storage input) {
        var parquetInput = (ParquetStorage) input;
        sparkSession.read()
                .options(parquetInput.getOptions())
                .parquet(parquetInput.getAbsolutePath())
                .createOrReplaceTempView(parquetInput.getId());
    }

    public boolean isCompatible(Storage input){return input instanceof ParquetStorage;}
}
