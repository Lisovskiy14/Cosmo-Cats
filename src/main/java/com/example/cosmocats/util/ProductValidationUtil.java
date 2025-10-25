package com.example.cosmocats.util;

import com.example.cosmocats.web.exception.ParamsValidationDetails;
import lombok.experimental.UtilityClass;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

import java.net.URI;
import java.util.List;

@UtilityClass
public class ProductValidationUtil {

    public static ProblemDetail getProblemDetailByValidationDetails(List<ParamsValidationDetails> validationDetails) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Request validation failed");
        problemDetail.setType(URI.create("urn:problem-type:validation-error"));
        problemDetail.setTitle("Failed Validation Exception");
        problemDetail.setProperty("validationErrors", validationDetails);
        return problemDetail;
    }
}
