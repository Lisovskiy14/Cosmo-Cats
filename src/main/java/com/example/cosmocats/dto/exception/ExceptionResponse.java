package com.example.cosmocats.dto.exception;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
public class ExceptionResponse {
    private int status;
    private String error;
    private String message;
    private String path;
}
