package com.data.spark.service.processor;

import com.data.spark.Target;
import com.data.spark.service.IncompatibleTypeException;
import com.data.spark.service.writer.OutputWriter;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@AllArgsConstructor
public class OutputProcessor {
    private List<OutputWriter> outputWriters;
    public void processOutput(Target target) {
        log.debug("Processing output: " + target.toString());
        var outputWriter = outputWriters.stream().filter(writer -> writer.isCompatible(target.getOutput())).findFirst();
        if (outputWriter.isPresent()){
            log.debug("Writing output...");
            outputWriter.get().writeOutput(target);
            log.debug("Output written successfully.");
        }
        else
            throw new IncompatibleTypeException("Output type not implemented yet: " + target.toString());
    }
}
