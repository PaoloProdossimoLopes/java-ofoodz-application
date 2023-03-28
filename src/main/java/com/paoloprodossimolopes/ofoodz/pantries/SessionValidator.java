package com.paoloprodossimolopes.ofoodz.pantries;

public interface SessionValidator {
    boolean validateUserIdentifier(String id);
    boolean validateSessionToken(String token);
}
