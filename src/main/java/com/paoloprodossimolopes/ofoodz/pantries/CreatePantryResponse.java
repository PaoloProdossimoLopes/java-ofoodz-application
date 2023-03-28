package com.paoloprodossimolopes.ofoodz.pantries;

public class CreatePantryResponse {
    private final Long id;
    private final String title;
    private final String sessionToken;

    public CreatePantryResponse(Long id, String title, String sessionToken) {
        this.id = id;
        this.title = title;
        this.sessionToken = sessionToken;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getSessionToken() {
        return sessionToken;
    }
}
