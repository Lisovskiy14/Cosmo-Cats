package com.example.cosmocats.web;

import com.example.cosmocats.featureToggle.exception.FeatureNotAvailableException;
import com.example.cosmocats.featureToggle.exception.FeatureNotFoundException;
import com.example.cosmocats.service.exception.ProductIdAlreadyExistsException;
import com.example.cosmocats.util.ProductValidationUtil;
import com.example.cosmocats.web.exception.ParamsValidationDetails;
import com.example.cosmocats.service.exception.ProductNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.net.URI;
import java.util.List;

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
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_PROBLEM_JSON)
                .body(ProductValidationUtil.getProblemDetailByValidationDetails(validationDetails));
    }


    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<Object> handleProductNotFound(ProductNotFoundException ex) {
        log.info("Product Not Found exception has occurred");
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        problemDetail.setType(URI.create("urn:problem-type:not-found"));
        problemDetail.setTitle("Product Not Found Exception");
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_PROBLEM_JSON)
                .body(problemDetail);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleInternalServerError(Exception ex) {
        log.info("Internal Server Error has occurred");
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        problemDetail.setType(URI.create("urn:problem-type:internal-server-error"));
        problemDetail.setTitle("Internal Server Error");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.APPLICATION_PROBLEM_JSON)
                .body(problemDetail);
    }

    @ExceptionHandler(ProductIdAlreadyExistsException.class)
    public ResponseEntity<Object> handleProductIdAlreadyExists(ProductIdAlreadyExistsException ex) {
        log.info("Product Id Already Exists exception has occurred");
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
        problemDetail.setType(URI.create("urn:problem-type:conflict-error"));
        problemDetail.setTitle("Product Id Already Exists Exception");
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(problemDetail);
    }

    @ExceptionHandler(FeatureNotAvailableException.class)
    public ResponseEntity<Object> handleFeatureNotAvailable(FeatureNotAvailableException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        problemDetail.setType(URI.create("urn:problem-type:not-found"));
        problemDetail.setTitle("Feature Not Available Exception");
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_PROBLEM_JSON)
                .body(problemDetail);
    }

    @ExceptionHandler(FeatureNotFoundException.class)
    public ResponseEntity<Object> handleFeatureNotAvailable(FeatureNotFoundException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        problemDetail.setType(URI.create("urn:problem-type:not-found"));
        problemDetail.setTitle("Feature Not Found Exception");
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_PROBLEM_JSON)
                .body(problemDetail);
    }
}
