package com.paoloprodossimolopes.ofoodz.pantries;

import java.util.Optional;

public class PantryCreatorController {
    private final PantryCreatorRepository repository;
    private final SessionValidator sessionValidator;

    public PantryCreatorController(PantryCreatorRepository repository, SessionValidator sessionValidator) {
        this.repository = repository;
        this.sessionValidator = sessionValidator;
    }

    public void create(PantryRequest request) {
        sessionValidator.validateSessionToken(request.getValidationToken());
        sessionValidator.validateUserIdentifier(request.getUserIdentifier());
        final Pantry pantry = new Pantry(Optional.empty(), request.getTitle());
        repository.save(pantry);
    }
}
