package com.soloval.tech.authenticator.directGrant;

import com.google.auto.service.AutoService;
import com.soloval.tech.constants.CommonConstants;
import com.soloval.tech.services.JPAService;
import com.soloval.tech.services.impl.JPAServiceImpl;
import com.soloval.tech.services.impl.UserServiceImpl;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.core.Response;
import lombok.NoArgsConstructor;
import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.authentication.AuthenticationFlowError;
import org.keycloak.authentication.Authenticator;
import org.keycloak.authentication.AuthenticatorFactory;
import org.keycloak.authentication.authenticators.directgrant.AbstractDirectGrantAuthenticator;
import org.keycloak.events.Errors;
import org.keycloak.models.*;
import org.keycloak.provider.ProviderConfigProperty;
import org.keycloak.services.managers.AuthenticationManager;

import java.util.LinkedList;
import java.util.List;

@AutoService(AuthenticatorFactory.class)
public class ValidateOtpPasswordAuthenticator extends AbstractDirectGrantAuthenticator {
    public static final String PROVIDER_ID = "otp-password-direct-grant";

    @Override
    public void authenticate(AuthenticationFlowContext context) {
        String otp = retrieveOTP(context);
        String transactionId = retrieveTransactionId(context);
        if (otp == null || transactionId == null || otp.isBlank() || transactionId.isBlank()) {
            context.getEvent().error(Errors.INVALID_REQUEST);
            Response challengeResponse = errorResponse(Response.Status.UNAUTHORIZED.getStatusCode(), "invalid_request", String.format("Missing parameter: %s or %s", CommonConstants.RequestFields.OTP, CommonConstants.RequestFields.TRANSACTION_ID));
            context.failure(AuthenticationFlowError.INVALID_USER, challengeResponse);
            return;
        }
        if (!isValid(context.getSession(), context.getUser(), otp, transactionId)) {
            context.getEvent().user(context.getUser());
            context.getEvent().error(Errors.INVALID_USER_CREDENTIALS);
            Response challengeResponse = errorResponse(Response.Status.UNAUTHORIZED.getStatusCode(), "invalid_grant", "Invalid user credentials");
            context.failure(AuthenticationFlowError.INVALID_USER, challengeResponse);
            return;
        }
        context.getAuthenticationSession().setAuthNote(AuthenticationManager.PASSWORD_VALIDATED, "true");
        context.success();
    }

    @Override
    public boolean requiresUser() {
        return true;
    }

    @Override
    public boolean configuredFor(KeycloakSession session, RealmModel realm, UserModel user) {
        return true;
    }

    @Override
    public void setRequiredActions(KeycloakSession session, RealmModel realm, UserModel user) {

    }

    @Override
    public boolean isUserSetupAllowed() {
        return false;
    }


    @Override
    public String getDisplayType() {
        return "OTP Password Validation";
    }

    @Override
    public String getReferenceCategory() {
        return null;
    }

    @Override
    public boolean isConfigurable() {
        return false;
    }

    @Override
    public AuthenticationExecutionModel.Requirement[] getRequirementChoices() {
        return REQUIREMENT_CHOICES;
    }

    @Override
    public String getHelpText() {
        return "Validates the password supplied as a 'otp' form parameter in direct grant request";
    }

    @Override
    public List<ProviderConfigProperty> getConfigProperties() {
        return new LinkedList<>();
    }

    @Override
    public String getId() {
        return PROVIDER_ID;
    }

    protected String retrieveOTP(AuthenticationFlowContext context) {
        MultivaluedMap<String, String> inputData = context.getHttpRequest().getDecodedFormParameters();
        return inputData.getFirst(CommonConstants.RequestFields.OTP);
    }

    protected String retrieveTransactionId(AuthenticationFlowContext context) {
        MultivaluedMap<String, String> inputData = context.getHttpRequest().getDecodedFormParameters();
        return inputData.getFirst(CommonConstants.RequestFields.TRANSACTION_ID);
    }

    private boolean isValid(KeycloakSession session, UserModel user, String otp, String transactionId) {
        JPAService jpaService = new JPAServiceImpl(session);
        return jpaService.verifyOTPVertificationCode(session.getContext().getRealm().getId(), user.getId(), otp, transactionId);
    }
}