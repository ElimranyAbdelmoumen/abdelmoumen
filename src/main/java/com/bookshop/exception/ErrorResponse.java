package com.bookshop.exception;

import java.time.Instant;
import java.util.List;

public class ErrorResponse {

    private int status;
    private String error;
    private String message;
    private Instant timestamp = Instant.now();
    private String path;
    private List<FieldError> fieldErrors;

    public ErrorResponse(int status, String error, String message, String path) {
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
    }

    public static ErrorResponse withFields(int status, String error, String message, String path, List<FieldError> fieldErrors) {
        ErrorResponse r = new ErrorResponse(status, error, message, path);
        r.setFieldErrors(fieldErrors);
        return r;
    }

    public static class FieldError {
        private String field;
        private String message;

        public FieldError(String field, String message) {
            this.field = field;
            this.message = message;
        }

        public String getField() {
            return field;
        }

        public String getMessage() {
            return message;
        }
    }

    public int getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public String getPath() {
        return path;
    }

    public List<FieldError> getFieldErrors() {
        return fieldErrors;
    }

    public void setFieldErrors(List<FieldError> fieldErrors) {
        this.fieldErrors = fieldErrors;
    }
}
