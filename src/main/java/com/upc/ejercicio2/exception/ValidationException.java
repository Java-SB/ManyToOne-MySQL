package com.upc.ejercicio2.exception;

public class ValidationException extends RuntimeException {
    public ValidationException() { super(); }
    public ValidationException(String message) {
        super(message);
    }
}