package com.paoloprodossimolopes.ofoodz.pantries.controllers;

import com.paoloprodossimolopes.ofoodz.pantries.*;
import net.bytebuddy.pool.TypePool;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PantryCreatorControllerTests {
    @Test
    @DisplayName("Ao inicialziar a controller nao deve realizar uma consulta para o repositorio")
    void test_onInitialize_noCallsRepository() {
        final Enviroment env = makeEnviroment();

        Assertions.assertEquals(env.repository.getCreateCount(), 0);
    }

    @Test
    @DisplayName("Ao realizar o request para o criar uma despensa chama SessionValidator para validar a sessão")
    void test_onCreate_callsSessionValidatorWithCorrectRequestParams() {
        final Enviroment env = makeEnviroment();
        final PantryRequest request = makePantryRequest();

        try { env.sut.create(request); }
        catch (Exception ignored) { }

        Assertions.assertEquals(env.sessionValidator.getTokenReceivedAt(0), request.getValidationToken());
        Assertions.assertEquals(env.sessionValidator.getUserIdentifierReceivedAt(0), request.getUserIdentifier());
    }

    @Test
    @DisplayName("Ao criar uma despensa com sucesso deve retornar uma responsta com uma despensa com ID")
    void test_onCreate_createsPantrySucessfully_respondsWithPantry() throws Exception {
        final Long pantryID = 1L;
        final Enviroment env = makeEnviroment();
        env.sessionValidator.setTokenValid();
        env.sessionValidator.setUserIdentifierValid();
        final PantryRequest request = makePantryRequest();
        env.repository.setSaveReturned(new Pantry(Optional.of(pantryID), request.getTitle()));

        CreatePantryResponse response = env.sut.create(request);

        Assertions.assertEquals(response.getTitle(), request.getTitle());
        Assertions.assertEquals(response.getId(), pantryID);
        Assertions.assertEquals(response.getSessionToken(), request.getValidationToken());
    }

    @Test
    @DisplayName("Ao criar valida se o token de sessao eh valido e devolve Unauthorized com status code 401")
    void test_onCreate_deleiversUnauthorized() {
        final Enviroment env = makeEnviroment();
        env.sessionValidator.setUserIdentifierValid();
        env.sessionValidator.setTokenInvalid();
        final PantryRequest request = makePantryRequest();

        UnauthorizedException exception = Assertions.assertThrows(UnauthorizedException.class, () -> env.sut.create(request));

        Assertions.assertEquals(exception.getMessage(), "User is unauthorized to perform this operation");
        Assertions.assertEquals(exception.getStatusCode(), 401);
    }

    @Test
    @DisplayName("Ao realizar o request para o endpoint com sessao valida deve chamar o repository para criar uma despensa")
    void test_onCreate_withValidPantryRequest_callsRepositoryWithRequestNameAndEmptyId() throws Exception {
        final Enviroment env = makeEnviroment();
        final PantryRequest request = makePantryRequest();
        env.sessionValidator.setTokenValid();
        env.sessionValidator.setUserIdentifierValid();

        env.sut.create(request);

        Assertions.assertEquals(env.repository.getCreateCount(), 1);
        Assertions.assertFalse(env.repository.getCreateReceivedAt(0).getId().isPresent());
        Assertions.assertEquals(env.repository.getCreateReceivedAt(0).getTitle(), request.getTitle());
    }

    @Test
    @DisplayName("Teste para assegurar que a despesa nao sera chamada caso o usuario nao exista")
    void test_onCreate_withInvalidUserIDCredential_noCallsRepositoryToCreatePantry() {
        final Enviroment env = makeEnviroment();
        final PantryRequest request = makePantryRequest();
        env.sessionValidator.setTokenValid();
        env.sessionValidator.setUserIdentifierInvalid();

        try { env.sut.create(request); }
        catch (Exception ignored) { }

        Assertions.assertEquals(env.repository.getCreateCount(), 0);
    }

    @Test
    @DisplayName("Ao chamar o create com id do usuario invalido retorna Unauthorized com status code 401")
    void test_onCreate_withInvalidUserIDCredential_throwsAnauthorizedWithStatusCode401() {
        final Enviroment env = makeEnviroment();
        final PantryRequest request = makePantryRequest();
        env.sessionValidator.setTokenValid();
        env.sessionValidator.setUserIdentifierInvalid();

        UnauthorizedException exception = Assertions.assertThrows(UnauthorizedException.class, () -> env.sut.create(request));
        Assertions.assertEquals(exception.getStatusCode(), 401);
        Assertions.assertEquals(exception.getMessage(), "User is unauthorized to perform this operation");
    }

    @Test
    @DisplayName("Teste para assegurar que a despesa nao sera chamada caso o token de sessao seja invalido")
    void test_onCreate_withInvalidSessionTokenCredential_noCallsRepositoryToCreatePantry() {
        final Enviroment env = makeEnviroment();
        final PantryRequest request = makePantryRequest();
        env.sessionValidator.setTokenInvalid();
        env.sessionValidator.setUserIdentifierValid();

        try { env.sut.create(request); }
        catch (Exception ignored) { }

        Assertions.assertEquals(env.repository.getCreateCount(), 0);
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
        final SessionValidatorStub sessionValidator = new SessionValidatorStub();
        final PantryCreatorController sut = new PantryCreatorController(repository, sessionValidator);

        return new Enviroment(sut, repository, sessionValidator);
    }

    private class Enviroment {
        final PantryCreatorController sut;
        final CreatorRepositorySpy repository;
        final SessionValidatorStub sessionValidator;

        public Enviroment(PantryCreatorController sut, CreatorRepositorySpy repository, SessionValidatorStub sessionValidator) {
            this.sut = sut;
            this.repository = repository;
            this.sessionValidator = sessionValidator;
        }
    }

    private class CreatorRepositorySpy implements PantryCreatorRepository {
        private List<Pantry> saveRequestsReceived = new ArrayList<>();
        private Pantry saveReturned = new Pantry(Optional.of(100L), "any default title");

        @Override
        public Pantry save(Pantry pantry) {
            saveRequestsReceived.add(pantry);
            return saveReturned;
        }

        int getCreateCount() {
            return saveRequestsReceived.size();
        }

        Pantry getCreateReceivedAt(int index) {
            return saveRequestsReceived.get(index);
        }

        public void setSaveReturned(Pantry pantry) {
            this.saveReturned = pantry;
        }

        public Pantry getSaveReturned() {
            return saveReturned;
        }
    }

    private class SessionValidatorStub implements SessionValidator {

        private boolean userIdentifierIsValid = false;
        private boolean userSessionTokenIsValid = false;

        private List<String> userIdentifiersReceived = new ArrayList<>();
        private List<String> userTokensReceived = new ArrayList<>();

        @Override
        public boolean validateUserIdentifier(String id) {
            userIdentifiersReceived.add(id);
            return userIdentifierIsValid;
        }

        @Override
        public boolean validateSessionToken(String token) {
            userTokensReceived.add(token);
            return userSessionTokenIsValid;
        }

        String getTokenReceivedAt(int index) {
            return userTokensReceived.get(index);
        }

        String getUserIdentifierReceivedAt(int index) {
            return userIdentifiersReceived.get(index);
        }

        void setTokenValid() {
            userSessionTokenIsValid = true;
        }

        void setTokenInvalid() {
            userSessionTokenIsValid = false;
        }

        void setUserIdentifierValid() {
            userIdentifierIsValid = true;
        }

        void setUserIdentifierInvalid() {
            userIdentifierIsValid = false;
        }
    }
}
