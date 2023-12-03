package com.data.spark.service.processor;

import com.data.spark.flow.BatchPlan;
import com.data.spark.service.transformer.Transformer;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

@Builder
@Slf4j
public class BatchDataProcessor implements DataProcessor{
    private InputProcessor inputProcessor;
    private OutputProcessor outputProcessor;
    private Transformer transformer;
    @Override
    public void process(BatchPlan batchPlan) {
        log.debug("Started processing inputs list: " + batchPlan.getInputs().toString());
        batchPlan.getInputs()
                .forEach(input -> {
                    inputProcessor.processInput(input);
                });
        log.debug("Finished processing inputs list");

        if (batchPlan.getTransformations() != null){
            log.debug("Started applying transformations: " + batchPlan.getTransformations().toString());
            batchPlan.getTransformations()
                    .forEach(transformation -> {
                        transformer.transform(transformation);
                    });
            log.debug("Ended applying transformations");
        }

        log.debug("Started processing targets list: " + batchPlan.getTargets().toString());
        batchPlan.getTargets()
                .forEach(target -> {
                    outputProcessor.processOutput(target);
                });
        log.debug("Finished processing outputs list");
    }
}
