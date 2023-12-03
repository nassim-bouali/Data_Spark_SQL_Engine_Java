package com.data.spark.service.writer;

import com.data.spark.JdbcStorage;
import com.data.spark.Storage;
import com.data.spark.Target;
import com.data.spark.service.configuration.StorageUtils;
import lombok.AllArgsConstructor;
import org.apache.spark.sql.SaveMode;
import org.apache.spark.sql.SparkSession;

@AllArgsConstructor
public class JdbcWriter implements OutputWriter{
    private SparkSession sparkSession;
    @Override
    public void writeOutput(Target target) {
        var jdbcOutput = (JdbcStorage) target.getOutput();
        var outputDataset = sparkSession.sql(target.sql());
        outputDataset.write()
                .mode(SaveMode.Overwrite)
                .jdbc(jdbcOutput.getUri(),
                        jdbcOutput.getTable(),
                        StorageUtils.getProperties(jdbcOutput.getOptions()));
    }
    public boolean isCompatible(Storage output){return output instanceof JdbcStorage;}
}
