package com.data.spark.service.transformer;

import com.data.spark.Transformation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.spark.sql.SparkSession;

@AllArgsConstructor
@Slf4j
public class SqlTransformer implements Transformer{
    private SparkSession sparkSession;
    @Override
    public void transform(Transformation transformation) {
        log.debug("Applying transformation: " + transformation.toString());
        sparkSession.sql(transformation.getSql())
                .createOrReplaceTempView(transformation.getId());
        log.debug("Transformation applied");
    }
}
