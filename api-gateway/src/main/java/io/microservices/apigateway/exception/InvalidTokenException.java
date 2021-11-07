package io.microservices.apigateway.exception;

public class InvalidTokenException extends RuntimeException {

    public InvalidTokenException(String message, Throwable throwable) {
        super(message, throwable);
    }

}
