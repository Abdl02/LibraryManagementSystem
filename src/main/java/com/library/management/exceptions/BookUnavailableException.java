package com.library.management.exceptions;

public class BookUnavailableException extends RuntimeException {
    public BookUnavailableException(String message) {
        super(message);
    }
}
