package com.data.spark.application;

import com.data.spark.flow.BatchPlan;
import com.data.spark.service.configuration.StorageUtils;
import com.data.spark.service.processor.BatchDataProcessor;
import com.data.spark.service.processor.DataProcessor;
import com.data.spark.service.processor.InputProcessor;
import com.data.spark.service.processor.OutputProcessor;
import com.data.spark.service.reader.CsvReader;
import com.data.spark.service.reader.JdbcReader;
import com.data.spark.service.reader.ParquetReader;
import com.data.spark.service.transformer.SqlTransformer;
import com.data.spark.service.transformer.Transformer;
import com.data.spark.service.writer.CsvWriter;
import com.data.spark.service.writer.JdbcWriter;
import com.data.spark.service.writer.ParquetWriter;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.spark.sql.SparkSession;
import picocli.CommandLine;

import java.io.File;
import java.util.Arrays;
import java.util.Base64;
import java.util.concurrent.Callable;

@Slf4j
@CommandLine.Command
public class Application implements Callable<Integer> {
    @CommandLine.Option(names = "--app-name", required = false)
    private String appName = "Batch processor";
    @CommandLine.Option(names = "--is-local", required = false)
    private boolean isLocal = true;
    @CommandLine.Option(names = "--inline", required = false)
    private boolean inline = true;
    @CommandLine.Option(names = "--plan", required = true)
    private String plan;

    public static void main(String[] args) {
        int exitCode = new CommandLine(new Application()).execute(args);
        System.exit(exitCode);
    }

    @Override
    public Integer call() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        BatchPlan batchPlan = null;

        if (inline) {
            batchPlan = objectMapper.readValue(Base64.getDecoder().decode(plan), BatchPlan.class);
        }
        else {
            batchPlan = objectMapper.readValue(new File(plan), BatchPlan.class);
        }

        SparkSession.Builder sparkSessionBuilder = SparkSession.builder().appName(appName);
        SparkSession sparkSession = null;
        if (isLocal) {
            sparkSession = sparkSessionBuilder.master("local[*]").getOrCreate();
        }

        var config = sparkSession.sparkContext().hadoopConfiguration();

        StorageUtils.updateHadoopConfig(batchPlan, config);

        InputProcessor inputProcessor = new InputProcessor(Arrays.asList(new CsvReader(sparkSession), new JdbcReader(sparkSession), new ParquetReader(sparkSession)));
        OutputProcessor outputProcessor = new OutputProcessor(Arrays.asList(new CsvWriter(sparkSession), new JdbcWriter(sparkSession), new ParquetWriter(sparkSession)));
        Transformer transformer = new SqlTransformer(sparkSession);

        DataProcessor dataProcessor = BatchDataProcessor.builder()
                .inputProcessor(inputProcessor)
                .outputProcessor(outputProcessor)
                .transformer(transformer)
                .build();

        dataProcessor.process(batchPlan);

        return 0;
    }
}
