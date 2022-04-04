package com.example.temp.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import lombok.Data;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;

/**
 *
 * @author AnuragSingh
 */
@Data
public class ErrorResponse {

    @Value("${log.trace:false}")
    private boolean logTrace;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private final LocalDateTime timestamp;
    private HttpStatus status;
    private String message;
    private String trace;

    private ErrorResponse() {
        this.timestamp = LocalDateTime.now();
    }

    public ErrorResponse(HttpStatus status) {
        this();
        this.status = status;
    }

    public ErrorResponse(HttpStatus status, String message) {
        this();
        this.status = status;
        this.message = message;
    }

    public ErrorResponse(HttpStatus status, Throwable ex) {
        this();
        this.status = status;
        this.message = "Unexpected error";
        if (logTrace) {
            this.trace = ExceptionUtils.getStackTrace(ex);
        }
    }

    public ErrorResponse(HttpStatus status, String message, Throwable ex) {
        this();
        this.status = status;
        this.message = message;
        if (logTrace) {
            this.trace = ExceptionUtils.getStackTrace(ex);
        }
    }
}
