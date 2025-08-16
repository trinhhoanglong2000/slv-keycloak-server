package com.soloval.tech.validator;

import com.google.auto.service.AutoService;
import com.soloval.tech.utils.KeycloakUtils;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;
import org.keycloak.models.UserProvider;
import org.keycloak.provider.ConfiguredProvider;
import org.keycloak.provider.ProviderConfigProperty;
import org.keycloak.validate.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@AutoService(ValidatorFactory.class)
public class UniqueAttributeValidatorProvider extends AbstractStringValidator implements ConfiguredProvider {
    public static final String ID = "unique-attribute";
    public static final String MESSAGE_ATTRIBUTE_NOT_UNIQUE = "%s must be unique";
    private static final List<ProviderConfigProperty> configProperties = new ArrayList<>();

    public UniqueAttributeValidatorProvider() {
    }

    @Override
    public String getId() {
        return ID;
    }

    @Override
    protected void doValidate(String attributeValue, String attributeName, ValidationContext context, ValidatorConfig config) {
        KeycloakSession session = context.getSession();

        UserModel currentUser = (UserModel) context.getAttributes().get(UserModel.class.getName());

        if (!isAttributeUnique(attributeValue, attributeName, session, currentUser)) {
            context.addError(new ValidationError(ID, attributeName, String.format(MESSAGE_ATTRIBUTE_NOT_UNIQUE, attributeName)));
        }
    }

    public boolean isAttributeUnique(String attributeValue, String attributeName, KeycloakSession session, UserModel currentUser) {

        var listUser = KeycloakUtils.getListUserByUserAttribute(session, attributeName, attributeValue);
        Stream<UserModel> usersWithSameAttributeValue = listUser.stream();

        return currentUser != null
                ? usersWithSameAttributeValue.filter(user -> !user.getId().equals(currentUser.getId())).findAny().isEmpty()
                : usersWithSameAttributeValue.findAny().isEmpty();
    }

    @Override
    public String getHelpText() {
        return "Unique Attribute";
    }

    @Override
    public List<ProviderConfigProperty> getConfigProperties() {
        return configProperties;
    }

}