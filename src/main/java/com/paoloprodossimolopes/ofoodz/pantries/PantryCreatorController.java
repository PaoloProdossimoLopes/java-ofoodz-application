package com.paoloprodossimolopes.ofoodz.pantries;

import java.util.Optional;

public class PantryCreatorController {
    private final PantryCreatorRepository repository;
    private final SessionValidator sessionValidator;

    public PantryCreatorController(PantryCreatorRepository repository, SessionValidator sessionValidator) {
        this.repository = repository;
        this.sessionValidator = sessionValidator;
    }

    public CreatePantryResponse create(PantryRequest request) throws Exception {
        final boolean userExists = sessionValidator.validateUserIdentifier(request.getUserIdentifier());
        final boolean tokenIsValid = sessionValidator.validateSessionToken(request.getValidationToken());

        if (userExists == false || tokenIsValid == false) {
            throw new UnauthorizedException();
        }

        final Pantry pantryCreated = repository.save(
                new Pantry(Optional.empty(), request.getTitle())
        );

        return new CreatePantryResponse(
                pantryCreated.getId().get(),
                pantryCreated.getTitle(),
                request.getValidationToken()
        );
    }
}
