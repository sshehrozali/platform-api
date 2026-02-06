package com.platform.api;

public class PulumiRunnerException extends RuntimeException {
    public PulumiRunnerException(String message, Exception e) {
        super(message, e);
    }
}
