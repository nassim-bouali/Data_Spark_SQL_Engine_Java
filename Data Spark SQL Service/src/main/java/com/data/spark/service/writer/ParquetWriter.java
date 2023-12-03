package com.data.spark.service.writer;

import com.data.spark.ParquetStorage;
import com.data.spark.Storage;
import com.data.spark.Target;
import lombok.AllArgsConstructor;
import org.apache.spark.sql.SaveMode;
import org.apache.spark.sql.SparkSession;

@AllArgsConstructor
public class ParquetWriter implements OutputWriter{
    private SparkSession sparkSession;
    @Override
    public void writeOutput(Target target) {
        var parquetOutput = (ParquetStorage) target.getOutput();
        var outputDataset = sparkSession.sql(target.sql());
        outputDataset.write()
                .options(parquetOutput.getOptions())
                .mode(SaveMode.Append)
                .parquet(parquetOutput.getAbsolutePath());
    }
    public boolean isCompatible(Storage output){return output instanceof ParquetStorage;}
}
