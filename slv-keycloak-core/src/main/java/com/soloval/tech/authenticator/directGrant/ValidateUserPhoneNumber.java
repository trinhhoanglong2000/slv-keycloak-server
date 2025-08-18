package com.soloval.tech.authenticator.directGrant;

import com.google.auto.service.AutoService;
import com.soloval.tech.constants.CommonConstants;
import com.soloval.tech.utils.KeycloakUtils;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.core.Response;
import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.authentication.AuthenticationFlowError;
import org.keycloak.authentication.AuthenticatorFactory;
import org.keycloak.authentication.authenticators.browser.AbstractUsernameFormAuthenticator;
import org.keycloak.authentication.authenticators.directgrant.AbstractDirectGrantAuthenticator;
import org.keycloak.authentication.authenticators.util.AuthenticatorUtils;
import org.keycloak.events.Details;
import org.keycloak.events.Errors;
import org.keycloak.models.*;
import org.keycloak.provider.ProviderConfigProperty;

import java.util.List;

import static com.soloval.tech.constants.KeycloakAttributesConstant.PHONE_ATTRIBUTE;
import static org.keycloak.authentication.authenticators.util.AuthenticatorUtils.getDisabledByBruteForceEventError;

@AutoService(AuthenticatorFactory.class)
public class ValidateUserPhoneNumber extends AbstractDirectGrantAuthenticator {
    public static final String PROVIDER_ID = "validate-user-phone-direct-grant";
    private final AuthenticationExecutionModel.Requirement[] REQUIREMENT_CHOICES = {
            AuthenticationExecutionModel.Requirement.REQUIRED
    };

    public void authenticate(AuthenticationFlowContext context) {
        String phoneNumber = retrievePhoneNumber(context);
        if (phoneNumber == null) {
            context.getEvent().error(Errors.USER_NOT_FOUND);
            Response challengeResponse = errorResponse(Response.Status.UNAUTHORIZED.getStatusCode(), "invalid_request", String.format("Missing parameter: %s", CommonConstants.RequestFields.PHONE_NUMBER));
            context.failure(AuthenticationFlowError.INVALID_USER, challengeResponse);
            return;
        }
        context.getEvent().detail(Details.USERNAME, phoneNumber);
        context.getAuthenticationSession().setAuthNote(AbstractUsernameFormAuthenticator.ATTEMPTED_USERNAME, phoneNumber);
        var listUserByUserAttribute = KeycloakUtils.getListUserByUserAttribute(context.getSession(), PHONE_ATTRIBUTE, phoneNumber);

        UserModel user = null;

        if (listUserByUserAttribute.isEmpty()) {
            AuthenticatorUtils.dummyHash(context);
            context.getEvent().error(Errors.USER_NOT_FOUND);
            Response challengeResponse = errorResponse(Response.Status.UNAUTHORIZED.getStatusCode(), "invalid_grant", "Invalid user credentials");
            context.failure(AuthenticationFlowError.INVALID_USER, challengeResponse);
            return;
        } else {
            user = listUserByUserAttribute.getFirst();
        }

        if (user == null) {
            AuthenticatorUtils.dummyHash(context);
            context.getEvent().error(Errors.USER_NOT_FOUND);
            Response challengeResponse = errorResponse(Response.Status.UNAUTHORIZED.getStatusCode(), "invalid_grant", "Invalid user credentials");
            context.failure(AuthenticationFlowError.INVALID_USER, challengeResponse);
            return;
        }

        String bruteForceError = getDisabledByBruteForceEventError(context, user);
        if (bruteForceError != null) {
            AuthenticatorUtils.dummyHash(context);
            context.getEvent().user(user);
            context.getEvent().error(bruteForceError);
            Response challengeResponse = errorResponse(Response.Status.UNAUTHORIZED.getStatusCode(), "invalid_grant", "Invalid user credentials");
            context.forceChallenge(challengeResponse);
            return;
        }

        if (!user.isEnabled()) {
            context.getEvent().user(user);
            context.getEvent().error(Errors.USER_DISABLED);
            Response challengeResponse = errorResponse(Response.Status.BAD_REQUEST.getStatusCode(), "invalid_grant", "Account disabled");
            context.forceChallenge(challengeResponse);
            return;
        }
        context.setUser(user);
        context.success();
    }

    public boolean requiresUser() {
        return false;
    }

    public boolean configuredFor(KeycloakSession session, RealmModel realm, UserModel user) {
        return false;
    }

    public void setRequiredActions(KeycloakSession session, RealmModel realm, UserModel user) {
    }

    public boolean isUserSetupAllowed() {
        return false;
    }

    public String getDisplayType() {
        return "PhoneNumber Validation";
    }

    public String getReferenceCategory() {
        return null;
    }

    public boolean isConfigurable() {
        return false;
    }

    public AuthenticationExecutionModel.Requirement[] getRequirementChoices() {
        return REQUIREMENT_CHOICES;
    }

    public String getHelpText() {
        return "Validates the username supplied as a 'phone' form parameter in direct grant request";
    }

    public List<ProviderConfigProperty> getConfigProperties() {
        return List.of();
    }

    public String getId() {
        return PROVIDER_ID;
    }

    protected String retrievePhoneNumber(AuthenticationFlowContext context) {
        MultivaluedMap<String, String> inputData = context.getHttpRequest().getDecodedFormParameters();
        return (String) inputData.getFirst(CommonConstants.RequestFields.PHONE_NUMBER);
    }
}
