package com.example.cosmocats.service.exception;

public class NoSuchResourceException extends RuntimeException {

    public NoSuchResourceException(String message) {
        super(message);
    }
}
