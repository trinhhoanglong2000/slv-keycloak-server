package com.soloval.tech.utils;

import org.keycloak.common.util.MultivaluedHashMap;
import org.keycloak.component.ComponentModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;
import org.keycloak.models.UserProvider;

import java.util.List;

public class KeycloakUtils {
    public static RealmModel getRealm(KeycloakSession session) {
        if (session.getContext() == null) {
            return null;
        }
        if (session.getContext().getRealm() != null) {
            return session.getContext().getRealm();
        }
        if (session.getContext().getAuthenticationSession() != null
                && session.getContext().getAuthenticationSession().getRealm() != null) {
            return session.getContext().getAuthenticationSession().getRealm();
        }
        if (session.getContext().getClient() != null
                && session.getContext().getClient().getRealm() != null) {
            return session.getContext().getClient().getRealm();
        }
        return null;
    }

    public static List<UserModel> getListUserByUserAttribute(KeycloakSession session, String attributeName, String attributeValue) {
        RealmModel realm = getRealm(session);
        UserProvider userProvider = session.getProvider(UserProvider.class);


        return userProvider.searchForUserByUserAttributeStream(realm, attributeName, attributeValue).toList();
    }

    public static List<ComponentModel> getListComponentByProviderId(KeycloakSession session, String providerId) {
        return session.getContext().getRealm().getComponentsStream().filter(e -> providerId.equals(e.getProviderId())).toList();
    }

    public static MultivaluedHashMap<String, String> getFirstComponentConfig(List<ComponentModel> componentModels) {
        if (componentModels.size() != 1) {
            return new MultivaluedHashMap<>();
        }
        return componentModels.getFirst().getConfig();
    }
}
