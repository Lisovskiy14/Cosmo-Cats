package com.example.cosmocats.web;

import com.example.cosmocats.featureToggle.exception.FeatureNotAvailableException;
import com.example.cosmocats.service.exception.conflict.ResourceAlreadyExistsException;
import com.example.cosmocats.service.exception.notFound.ResourceNotFoundException;
import com.example.cosmocats.util.ProblemDetailBuilder;
import com.example.cosmocats.util.ProductValidationUtil;
import com.example.cosmocats.web.exception.ParamsValidationDetails;
import com.example.cosmocats.service.exception.notFound.ProductNotFoundException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.net.URI;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request
    ) {
        log.info("Validation Error has occurred");
        List<ParamsValidationDetails> validationDetails = ex.getFieldErrors().stream()
                .map(err -> ParamsValidationDetails.builder()
                        .field(err.getField())
                        .message(err.getDefaultMessage())
                        .build()
                )
                .toList();

        return buildValidationErrorResponse(validationDetails);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex) {
        log.info("Method Argument Type Mismatch Exception has occurred");
        ProblemDetail problemDetail = ProblemDetailBuilder.builder()
                .status(HttpStatus.BAD_REQUEST)
                .type(URI.create("urn:problem-type:validation-error"))
                .title("Failed Validation Exception")
                .detail(ex.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_PROBLEM_JSON)
                .body(problemDetail);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException ex) {
        log.info("Constraint Validation Error has occurred");
        List<ParamsValidationDetails> validationDetails = ex.getConstraintViolations().stream()
                .map(err -> {
                    String path = err.getPropertyPath().toString();
                    return ParamsValidationDetails.builder()
                            .field(path.substring(path.lastIndexOf('.') + 1))
                            .message(err.getMessage())
                            .build();
                })
                .toList();

        return buildValidationErrorResponse(validationDetails);
    }


    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> handleProductNotFound(ResourceNotFoundException ex) {
        log.info("Resource Not Found exception has occurred");
        ProblemDetail problemDetail = ProblemDetailBuilder.builder()
                .status(HttpStatus.NOT_FOUND)
                .type(URI.create("urn:problem-type:not-found"))
                .title("Resource Not Found Exception")
                .detail(ex.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_PROBLEM_JSON)
                .body(problemDetail);
    }

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<Object> handleProductIdAlreadyExists(ResourceAlreadyExistsException ex) {
        log.info("Resource Already Exists exception has occurred");
        ProblemDetail problemDetail = ProblemDetailBuilder.builder()
                .status(HttpStatus.CONFLICT)
                .type(URI.create("urn:problem-type:conflict-error"))
                .title("Resource Already Exists Exception")
                .detail(ex.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(problemDetail);
    }

    @ExceptionHandler(FeatureNotAvailableException.class)
    public ResponseEntity<Object> handleFeatureNotAvailable(FeatureNotAvailableException ex) {
        ProblemDetail problemDetail = ProblemDetailBuilder.builder()
                .status(HttpStatus.NOT_FOUND)
                .type(URI.create("urn:problem-type:feature-not-found"))
                .title("Feature Not Available Exception")
                .detail(ex.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_PROBLEM_JSON)
                .body(problemDetail);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> handleInternalServerError(RuntimeException ex) {
        log.info("Internal Server Error has occurred");
        ProblemDetail problemDetail = ProblemDetailBuilder.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .type(URI.create("urn:problem-type:internal-server-error"))
                .title("Internal Server Error")
                .detail(ex.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.APPLICATION_PROBLEM_JSON)
                .body(problemDetail);
    }

    private ResponseEntity<Object> buildValidationErrorResponse(List<ParamsValidationDetails> errors) {
        ProblemDetail problemDetail = ProblemDetailBuilder.builder()
                .status(HttpStatus.BAD_REQUEST)
                .type(URI.create("urn:problem-type:validation-error"))
                .title("Failed Validation Exception")
                .detail("Request validation failed")
                .property("validationErrors", errors)
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_PROBLEM_JSON)
                .body(problemDetail);
    }
}
