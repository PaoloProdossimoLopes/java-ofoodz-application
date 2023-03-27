package com.paoloprodossimolopes.ofoodz.pantries;

public class PantryRequest {
    private final String title;
    private final String userIdentifier;
    private final String validationToken;

    public PantryRequest(String title, String userIdentifier, String validationToken) {
        this.title = title;
        this.userIdentifier = userIdentifier;
        this.validationToken = validationToken;
    }

    public String getTitle() {
        return title;
    }

    public String getUserIdentifier() {
        return userIdentifier;
    }

    public String getValidationToken() {
        return validationToken;
    }
}
