package com.soloval.tech.services.impl;

import com.soloval.tech.exception.AppException;
import com.soloval.tech.services.UserService;
import com.soloval.tech.utils.KeycloakUtils;
import jakarta.ws.rs.core.Response;
import org.apache.http.HttpStatus;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.UserModel;
import org.keycloak.services.resources.admin.UserResource;
import org.keycloak.userprofile.UserProfile;
import org.keycloak.userprofile.UserProfileProvider;
import org.keycloak.userprofile.ValidationException;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.*;

import static org.keycloak.representations.idm.AbstractUserRepresentation.*;
import static org.keycloak.userprofile.UserProfileContext.USER_API;

public class UserServiceImpl implements UserService {
    private final KeycloakSession session;

    public UserServiceImpl(KeycloakSession session) {
        this.session = session;
    }


    @Override
    public UserModel createNewUser(String username, String password, String firstName, String lastName, String email, Map<String, List<String>> attributes) {
        try {
            Map<String, List<String>> attrs = new HashMap();
            if (username != null) {
                attrs.put(USERNAME, Collections.singletonList(username));
            }
            if (email != null) {
                attrs.put(EMAIL, Collections.singletonList(email));
            }

            if (lastName != null) {
                attrs.put(LAST_NAME, Collections.singletonList(lastName));
            }

            if (firstName != null) {
                attrs.put(FIRST_NAME, Collections.singletonList(firstName));
            }
            attrs.putAll(attributes);

            var realm = KeycloakUtils.getRealm(session);

//        UserModel newUserModel = session.users().addUser(realm, username);
//
//        newUserModel.setEnabled(true);
//        newUserModel.setUsername(username);
//        newUserModel.setEmail(email);
//        newUserModel.setSingleAttribute(PHONE_ATTRIBUTE, phoneNumber);
//        newUserModel.setCreatedTimestamp(ZonedDateTime.now(ZoneOffset.UTC).toInstant().toEpochMilli());
//
//        var passwordProvider = (PasswordCredentialProvider) session.getProvider(CredentialProvider.class, PasswordCredentialProviderFactory.PROVIDER_ID);
//        passwordProvider.createCredential(realm, newUserModel, password);


            //

            UserProfileProvider profileProvider = session.getProvider(UserProfileProvider.class);

            UserProfile profile = profileProvider.create(USER_API, attrs);
            profile.validate();
            UserModel user = profile.create();
            user.setEnabled(true);
            user.setCreatedTimestamp(ZonedDateTime.now(ZoneOffset.UTC).toInstant().toEpochMilli());

            return user;
        } catch (ValidationException throwable) {
            throw new AppException(Response.Status.BAD_REQUEST.getStatusCode(), "", "", throwable.getErrors().stream().map(ValidationException.Error::getMessage).toList());
        }
    }
}
