package com.benchpress200.searchsyncprocessor.singlework.consumer.exception;

public abstract class NonRetryableEventException extends RuntimeException {
    protected NonRetryableEventException(String message) {
      super(message);
    }

    protected NonRetryableEventException(String message, Throwable cause) {
      super(message, cause);
    }
}
