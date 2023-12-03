package com.data.spark.service.reader;

import com.data.spark.Storage;

public interface InputReader {
    public void readInput(Storage input);
    public boolean isCompatible(Storage input);
}
