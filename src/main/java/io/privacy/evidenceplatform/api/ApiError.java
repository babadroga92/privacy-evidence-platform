package io.privacy.evidenceplatform.api;

import java.time.OffsetDateTime;

public class ApiError {
    private final OffsetDateTime timeStamp;
    private final int status;
    private final String error;
    private final String message;
    private final String path;

    public ApiError(OffsetDateTime timeStamp, int status, String error, String message, String path) {
        this.timeStamp = timeStamp;
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
    }

    public OffsetDateTime getTimeStamp() {
        return timeStamp;
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

    public String getPath() {
        return path;
    }
}
