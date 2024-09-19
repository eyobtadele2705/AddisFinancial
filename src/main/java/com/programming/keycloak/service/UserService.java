package com.programming.keycloak.service;

import com.programming.keycloak.model.User;
import com.programming.keycloak.model.records.NewUser;
import jakarta.servlet.http.HttpServletRequest;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.GroupRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;

import java.util.List;

public interface UserService {

    void createUser(NewUser newUser, HttpServletRequest request);
    void sendVerificationEmail(String userId);
    void deleteUser(String userId);
    void forgotPassword(String username);

    UserResource getUser(String userId);
    List<RoleRepresentation> getUserRoles(String userId);
    List<GroupRepresentation> getUserGroups(String userId);

    User findUserById(String userId);
}
