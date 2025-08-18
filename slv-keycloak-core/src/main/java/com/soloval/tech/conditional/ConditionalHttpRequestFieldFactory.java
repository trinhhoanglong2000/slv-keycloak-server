package com.soloval.tech.conditional;

import com.google.auto.service.AutoService;
import com.soloval.tech.constants.KeycloakAttributesConstant;
import org.keycloak.Config;
import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.authentication.AuthenticationFlowError;
import org.keycloak.authentication.AuthenticationFlowException;
import org.keycloak.authentication.AuthenticatorFactory;
import org.keycloak.authentication.authenticators.conditional.ConditionalAuthenticator;
import org.keycloak.authentication.authenticators.conditional.ConditionalAuthenticatorFactory;
import org.keycloak.authentication.authenticators.conditional.ConditionalUserAttributeValue;
import org.keycloak.authentication.authenticators.conditional.ConditionalUserAttributeValueFactory;
import org.keycloak.models.*;
import org.keycloak.models.utils.KeycloakModelUtils;
import org.keycloak.provider.ProviderConfigProperty;
import org.keycloak.provider.ProviderConfigurationBuilder;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

import static com.soloval.tech.constants.CommonConstants.*;
import static com.soloval.tech.constants.KeycloakAttributesConstant.CONDITIONAL_HTTP_FIELD_TYPE;

@AutoService(AuthenticatorFactory.class)
public class ConditionalHttpRequestFieldFactory implements ConditionalAuthenticatorFactory {

    public static final String PROVIDER_ID = "conditional-http-request-field";

    private final AuthenticationExecutionModel.Requirement[] REQUIREMENT_CHOICES = {
            AuthenticationExecutionModel.Requirement.REQUIRED, AuthenticationExecutionModel.Requirement.DISABLED
    };

    @Override
    public void init(Config.Scope config) {
        // no-op
    }

    @Override
    public void postInit(KeycloakSessionFactory factory) {
        // no-op
    }

    @Override
    public void close() {
        // no-op
    }

    @Override
    public String getId() {
        return PROVIDER_ID;
    }

    @Override
    public String getDisplayType() {
        return "Condition - http request field";
    }

    @Override
    public boolean isConfigurable() {
        return true;
    }

    @Override
    public AuthenticationExecutionModel.Requirement[] getRequirementChoices() {
        return REQUIREMENT_CHOICES;
    }

    @Override
    public boolean isUserSetupAllowed() {
        return false;
    }

    @Override
    public String getHelpText() {
        return "Flow is executed only if the the request params or request body contain a expected field that has the expected value";
    }

    @Override
    public List<ProviderConfigProperty> getConfigProperties() {
        final ProviderConfigurationBuilder builder = ProviderConfigurationBuilder.create();
        builder.property()
                .name(CONDITIONAL_HTTP_FIELD_TYPE)
                .label("Field Type")
                .type(ProviderConfigProperty.LIST_TYPE)
                .options(List.of(BODY, PATH_VARIABLE, QUERY_PARAMETER))
                .defaultValue(QUERY_PARAMETER)
                .helpText("Field Type to check")
                .add();

        builder.property()
                .name(KeycloakAttributesConstant.CONDITIONAL_HTTP_FIELD_NAME)
                .label("Field Name")
                .type(ProviderConfigProperty.STRING_TYPE)
                .defaultValue("")
                .helpText("Field Name to check")
                .add();

        builder.property()
                .name(KeycloakAttributesConstant.CONDITIONAL_HTTP_FIELD_EXPECTED_VALUE)
                .label("Field expected value")
                .type(ProviderConfigProperty.STRING_TYPE)
                .defaultValue("")
                .helpText("Expected value of the field")
                .add();

        return builder.build();
    }

    @Override
    public ConditionalAuthenticator getSingleton() {
        return ConditionalHttpRequestField.SINGLETON;
    }
}