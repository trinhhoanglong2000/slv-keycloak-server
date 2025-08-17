package com.soloval.tech.conditional;

import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.authentication.AuthenticationFlowError;
import org.keycloak.authentication.AuthenticationFlowException;
import org.keycloak.authentication.authenticators.conditional.ConditionalAuthenticator;
import org.keycloak.authentication.authenticators.conditional.ConditionalUserAttributeValueFactory;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;
import org.keycloak.models.utils.KeycloakModelUtils;

import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

import static com.soloval.tech.constants.CommonConstants.*;
import static com.soloval.tech.constants.KeycloakAttributesConstant.*;

public class ConditionalHttpRequestField implements ConditionalAuthenticator {
    static final ConditionalHttpRequestField SINGLETON = new ConditionalHttpRequestField();

    @Override
    public boolean matchCondition(AuthenticationFlowContext context) {
        // Retrieve configuration
        Map<String, String> config = context.getAuthenticatorConfig().getConfig();
        String fieldType = config.get(CONDITIONAL_HTTP_FIELD_TYPE);
        String fieldName = config.get(CONDITIONAL_HTTP_FIELD_NAME);
        String fieldExpectedValue = config.get(CONDITIONAL_HTTP_FIELD_EXPECTED_VALUE);

        if (fieldType == null || fieldName == null || fieldExpectedValue == null) {
            throw new AuthenticationFlowException("Error loading conditional configuration", AuthenticationFlowError.INTERNAL_ERROR);
        }
        String attributeValue = switch  (fieldType){
            case BODY -> context.getHttpRequest().getDecodedFormParameters().getFirst(fieldName);
            case QUERY_PARAMETER -> context.getHttpRequest().getUri().getQueryParameters().getFirst(fieldName);
            case PATH_VARIABLE -> context.getHttpRequest().getUri().getPathParameters().getFirst(fieldName);
            default -> throw new AuthenticationFlowException("Error loading conditional configuration", AuthenticationFlowError.INTERNAL_ERROR);
        };

        return attributeValue != null && attributeValue.equals(fieldExpectedValue);
    }

    @Override
    public void action(AuthenticationFlowContext context) {
        // Not used
    }

    @Override
    public boolean requiresUser() {
        return true;
    }

    @Override
    public void setRequiredActions(KeycloakSession session, RealmModel realm, UserModel user) {
        // Not used
    }

    @Override
    public void close() {
        // Does nothing
    }
}
