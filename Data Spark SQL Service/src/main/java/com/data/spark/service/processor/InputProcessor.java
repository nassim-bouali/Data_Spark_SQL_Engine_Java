package com.data.spark.service.processor;

import com.data.spark.Storage;
import com.data.spark.service.IncompatibleTypeException;
import com.data.spark.service.reader.InputReader;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@AllArgsConstructor
public class InputProcessor{
    private List<InputReader> inputReaders;
    public void processInput(Storage input) {
        log.debug("Processing input: " + input.toString());
        var inputReader = inputReaders.stream().filter(reader -> reader.isCompatible(input)).findFirst();
        if (inputReader.isPresent()){
            log.debug("Reading input...");
            inputReader.get().readInput(input);
            log.debug("Input read successfully.");
        }
        else
            throw new IncompatibleTypeException("Input type not implemented yet: " + input.toString());
    }
}
