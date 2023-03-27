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
        final boolean userExists = sessionValidator.validateUserIdentifier(request.getUserIdentifier());
        final boolean tokenIsValid = sessionValidator.validateSessionToken(request.getValidationToken());

        if (userExists && tokenIsValid) {
            final Pantry pantry = new Pantry(Optional.empty(), request.getTitle());
            repository.save(pantry);
        }
    }
}
