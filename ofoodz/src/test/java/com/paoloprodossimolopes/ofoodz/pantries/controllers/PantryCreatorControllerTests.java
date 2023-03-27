package com.paoloprodossimolopes.ofoodz.pantries.controllers;

import com.paoloprodossimolopes.ofoodz.pantries.PantryCreatorRepository;
import com.paoloprodossimolopes.ofoodz.pantries.Pantry;
import com.paoloprodossimolopes.ofoodz.pantries.PantryCreatorController;
import com.paoloprodossimolopes.ofoodz.pantries.PantryRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class PantryCreatorControllerTests {
    @Test
    void test_onInitialize_noCallsRepository() {
        final Enviroment env = makeEnviroment();

        Assertions.assertEquals(env.repository.getCreateCount(), 0);
    }

    @Test
    void test_onCreate_withValidPantryRequest_callsRepositoryWithRequestNameAndEmptyId() {
        final Enviroment env = makeEnviroment();
        final PantryRequest request = makePantryRequest();

        env.sut.create(request);

        Assertions.assertEquals(env.repository.getCreateCount(), 1);
        Assertions.assertFalse(env.repository.getCreateReceivedAt(0).getId().isPresent());
        Assertions.assertEquals(env.repository.getCreateReceivedAt(0).getTitle(), request.getTitle());
    }

    private PantryRequest makePantryRequest() {
        return new PantryRequest(
                "any name for pantry",
                "any user identifier",
                "any valid token"
        );
    }

    private Enviroment makeEnviroment() {
        final CreatorRepositorySpy repository = new CreatorRepositorySpy();
        final PantryCreatorController sut = new PantryCreatorController(repository);
        return new Enviroment(sut, repository);
    }

    private class Enviroment {
        final PantryCreatorController sut;
        final CreatorRepositorySpy repository;

        public Enviroment(PantryCreatorController sut, CreatorRepositorySpy repository) {
            this.sut = sut;
            this.repository = repository;
        }
    }

    private class CreatorRepositorySpy implements PantryCreatorRepository {
        private List<Pantry> saveRequestsReceived = new ArrayList<>();

        @Override
        public void save(Pantry pantry) {
            saveRequestsReceived.add(pantry);
        }

        int getCreateCount() {
            return saveRequestsReceived.size();
        }

        Pantry getCreateReceivedAt(int index) {
            return saveRequestsReceived.get(index);
        }
    }
}
