package com.data.spark.service.processor;

import com.data.spark.flow.BatchPlan;

public interface DataProcessor {
    public void process(BatchPlan batchPlan);
}
