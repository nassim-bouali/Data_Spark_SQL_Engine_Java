package com.data.spark.service;

public class IncompatibleTypeException extends RuntimeException {

    public IncompatibleTypeException() {
    }
    public IncompatibleTypeException(String message) {
        super(message);
    }
}
