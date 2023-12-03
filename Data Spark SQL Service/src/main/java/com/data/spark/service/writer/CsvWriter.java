package com.data.spark.service.writer;

import com.data.spark.CsvStorage;
import com.data.spark.Storage;
import com.data.spark.Target;
import lombok.AllArgsConstructor;
import org.apache.spark.sql.SaveMode;
import org.apache.spark.sql.SparkSession;

@AllArgsConstructor
public class CsvWriter implements OutputWriter{
    private SparkSession sparkSession;
    @Override
    public void writeOutput(Target target) {
        var csvOutput = (CsvStorage) target.getOutput();
        var outputDataset = sparkSession.sql(target.sql());
        outputDataset.write()
                .options(csvOutput.getOptions())
                .mode(SaveMode.Append)
                .csv(csvOutput.getAbsolutePath());
    }
    public boolean isCompatible(Storage output){return output instanceof CsvStorage;}
}
