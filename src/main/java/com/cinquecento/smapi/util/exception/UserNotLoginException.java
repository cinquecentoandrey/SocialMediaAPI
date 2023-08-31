package com.cinquecento.smapi.util.exception;

public class UserNotLoginException extends RuntimeException {
    public UserNotLoginException(String message, Throwable cause) {
        super(message, cause);
    }
}
