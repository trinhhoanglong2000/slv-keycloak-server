package com.soloval.tech.utils;

import jakarta.ws.rs.ForbiddenException;
import jakarta.ws.rs.NotAuthorizedException;
import org.keycloak.models.KeycloakSession;
import org.keycloak.services.managers.AppAuthManager;
import org.keycloak.services.managers.AuthenticationManager;

import static com.soloval.tech.constants.KeycloakAttributesConstant.CLIENT_ID;


public enum Authenticator {
    INSTANCE;

    private Authenticator() {
    }

    public AuthenticationManager.AuthResult authenticate(KeycloakSession session) {
        AuthenticationManager.AuthResult authResult = new AppAuthManager.BearerTokenAuthenticator(session).authenticate();
        if (authResult == null || authResult.getToken().getIssuedFor() == null) {
            throw new NotAuthorizedException("Not authorized for this resource");
        }
        if (!authResult.getToken().getOtherClaims().getOrDefault(CLIENT_ID, "").equals(authResult.getToken().getIssuedFor())) {
            throw new ForbiddenException("Not authorized for this resource");
        }

        return authResult;
    }
}