package com.soloval.tech.services;

import org.keycloak.models.UserModel;

import java.util.List;
import java.util.Map;

public interface UserService {

    UserModel createNewUser(String username, String password, String firstName, String lastName, String email, Map<String, List<String>> attributes);
}
