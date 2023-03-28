package com.paoloprodossimolopes.ofoodz.pantries;

public class UnauthorizedException extends Exception {
    private final int statusCode = 401;
    private final String message = "User is unauthorized to perform this operation";

    public UnauthorizedException() { }

    public int getStatusCode() {
        return statusCode;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
