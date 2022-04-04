package com.example.temp.exception;

import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 *
 * @author AnuragSingh
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> defaultExcpetionHandler(Exception ex, WebRequest request) {
        logError(new Throwable().getStackTrace()[0].getMethodName(), ex);
        return buildResponseEntity(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex));
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Object> notFoundException(NotFoundException ex, WebRequest request) {
        logError(new Throwable().getStackTrace()[0].getMethodName(), ex);
        return buildResponseEntity(new ErrorResponse(HttpStatus.NOT_FOUND, ex.getLocalizedMessage()));
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
            HttpHeaders headers, HttpStatus status, WebRequest request) {

        logError(new Throwable().getStackTrace()[0].getMethodName(), ex);
        StringBuilder msg = new StringBuilder("");

        ex.getBindingResult().getAllErrors().forEach(e -> {
            String fieldName = ((FieldError) e).getField();
            String message = e.getDefaultMessage();
            msg.append(fieldName)
                    .append("=")
                    .append(message)
                    .append(";");
        });
        ErrorResponse response = new ErrorResponse(HttpStatus.BAD_REQUEST);
        response.setMessage(msg.toString());
        return buildResponseEntity(response);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex,
            WebRequest request) {

        logError(new Throwable().getStackTrace()[0].getMethodName(), ex);

        ErrorResponse response = new ErrorResponse(HttpStatus.BAD_REQUEST);
        response.setMessage(String.format("Parameter '%s' of value '%s' could not be converted to type '%s'",
                ex.getName(), ex.getValue(), ex.getRequiredType().getSimpleName()));
        return buildResponseEntity(response);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException ex) {

        logError(new Throwable().getStackTrace()[0].getMethodName(), ex);

        Set<ConstraintViolation<?>> violations = ex.getConstraintViolations();
        String msg;

        if (!violations.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            violations.forEach(v -> {
                sb.append(" ")
                        .append(v.getPropertyPath())
                        .append("=")
                        .append(v.getMessage());
            });
            msg = sb.toString().trim();
        } else {
            msg = "ConstraintViolationException error";
        }

        ErrorResponse response = new ErrorResponse(HttpStatus.BAD_REQUEST);
        response.setMessage(msg);
        return buildResponseEntity(response);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex,
            HttpHeaders headers, HttpStatus status, WebRequest request) {
        logError(new Throwable().getStackTrace()[0].getMethodName(), ex);
        return buildResponseEntity(new ErrorResponse(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), ex));
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestPart(MissingServletRequestPartException ex,
            HttpHeaders headers, HttpStatus status, WebRequest request) {
        logError(new Throwable().getStackTrace()[0].getMethodName(), ex);
        return buildResponseEntity(new ErrorResponse(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), ex));
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
            HttpHeaders headers, HttpStatus status, WebRequest request) {
        logError(new Throwable().getStackTrace()[0].getMethodName(), ex);

        ServletWebRequest servletWebRequest = (ServletWebRequest) request;
        log.error("{} to {}", servletWebRequest.getHttpMethod(), servletWebRequest.getRequest().getServletPath());

        return buildResponseEntity(new ErrorResponse(HttpStatus.BAD_REQUEST, "Malformed JSON request", ex));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Object> handleDataIntegrityViolation(DataIntegrityViolationException ex,
            WebRequest request) {
        logError(new Throwable().getStackTrace()[0].getMethodName(), ex);

        if (ex.getCause() instanceof ConstraintViolationException) {
            return buildResponseEntity(new ErrorResponse(HttpStatus.CONFLICT, "Database error", ex.getCause()));
        }
        return buildResponseEntity(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Data Integrity Violation", ex));
    }

    @ExceptionHandler(RestClientResponseException.class)
    public ResponseEntity<Object> handleRestResponseException(RestClientResponseException ex,
            HttpHeaders headers, HttpStatus status, WebRequest request) {
        logError(new Throwable().getStackTrace()[0].getMethodName(), ex);
        return buildResponseEntity(new ErrorResponse(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), ex));
    }

    private void logError(String methodName, Exception ex) {
        log.error("--[" + this.getClass().getSimpleName() + "::" + methodName + "]--");
        log.error(ex.toString() + "\n");
    }

    private ResponseEntity<Object> buildResponseEntity(ErrorResponse errorResponse) {
        return new ResponseEntity<>(errorResponse, errorResponse.getStatus());
    }

    private HttpStatus getStatus(HttpServletRequest request) {
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        if (statusCode == null) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return HttpStatus.valueOf(statusCode);
    }
}
