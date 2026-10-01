package com.tap.staynest.exception;

public class PGNotFoundException extends RuntimeException {
    public PGNotFoundException(String message) {
        super(message);
    }
}
