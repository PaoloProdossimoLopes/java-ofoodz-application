package com.paoloprodossimolopes.ofoodz.pantries.controllers;

import com.paoloprodossimolopes.ofoodz.pantries.CreatorRepository;
import com.paoloprodossimolopes.ofoodz.pantries.PantryCreatorController;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PantryCreatorControllerTests {
    @Test
    void test_onInitialize_noCallsRepository() {
        final CreatorRepositorySpy repository = new CreatorRepositorySpy();

        new PantryCreatorController(repository);

        Assertions.assertEquals(repository.getCreatCallCount(), 0);
    }

    private class CreatorRepositorySpy implements CreatorRepository {
        private int createCallCount = 0;

        int getCreatCallCount() {
            return createCallCount;
        }
    }
}
