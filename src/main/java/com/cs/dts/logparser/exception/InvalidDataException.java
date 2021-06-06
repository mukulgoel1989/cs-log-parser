package com.cs.dts.logparser.exception;

public class InvalidDataException extends RuntimeException {

    public InvalidDataException(String message) {
        super(message);
    }

    public InvalidDataException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
