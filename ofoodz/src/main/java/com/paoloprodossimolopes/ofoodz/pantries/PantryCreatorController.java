package com.paoloprodossimolopes.ofoodz.pantries;

import java.util.Optional;

public class PantryCreatorController {
    private final PantryCreatorRepository repository;

    public PantryCreatorController(PantryCreatorRepository repository) {
        this.repository = repository;
    }

    public void create(PantryRequest request) {
        final Pantry pantry = new Pantry(Optional.empty(), request.getTitle());
        repository.save(pantry);
    }
}
