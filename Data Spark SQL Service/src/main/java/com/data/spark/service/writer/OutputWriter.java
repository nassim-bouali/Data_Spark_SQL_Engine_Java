package com.data.spark.service.writer;

import com.data.spark.Storage;
import com.data.spark.Target;

public interface OutputWriter {
    public void writeOutput(Target target);
    public boolean isCompatible(Storage output);
}
