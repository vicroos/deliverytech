package com.deliverytech.delivery.api.exception;

import java.util.List;

public record ErrorResponse(int status, String message, long timestamp, List<ValidationError> errors) {
        public ErrorResponse(int status, String message, long timestamp) {
        this(status, message, timestamp, null);
    }

    public record ValidationError(String message, String field) {}
}