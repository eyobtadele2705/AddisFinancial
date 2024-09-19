package com.programming.keycloak.service.impl;

import com.programming.keycloak.event.RegistrationCompleteEvent;
import com.programming.keycloak.model.User;
import com.programming.keycloak.model.records.NewUser;
import com.programming.keycloak.model.token.VerificationToken;
import com.programming.keycloak.repository.TokenVerificationRepository;
import com.programming.keycloak.repository.UserDao;
import com.programming.keycloak.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.GroupRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.Calendar;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Value("${app.keycloak.realm}")
    private String realm;
    @Value("${application.mailing.activation-url}")
    private String activationUrl;
    private final Keycloak keycloak;

    private final PasswordEncoder encoder;
    private final UserDao userDao;
    private final TokenVerificationRepository verificationRepository;
    private final ApplicationEventPublisher eventPublisher;
    @Override
    public void createUser(NewUser newUser, HttpServletRequest request) {

        UserRepresentation  userRepresentation= new UserRepresentation();
        userRepresentation.setEnabled(true);
        userRepresentation.setFirstName(newUser.firstName());
        userRepresentation.setLastName(newUser.lastName());
        userRepresentation.setUsername(newUser.username());
        userRepresentation.setEmail(newUser.username());
        userRepresentation.setEmailVerified(false);

        CredentialRepresentation credentialRepresentation=new CredentialRepresentation();
        credentialRepresentation.setValue(newUser.password());
        credentialRepresentation.setType(CredentialRepresentation.PASSWORD);

        userRepresentation.setCredentials(List.of(credentialRepresentation));

        UsersResource usersResource = getUsersResource();

        Response response = usersResource.create(userRepresentation);

        String location = response.getHeaderString("Location");
        int i = location.lastIndexOf("/");

        String userId = location.substring(i + 1);

        log.info("User Id is " + userId);
        log.info("Status Code "+response.getStatus());

        var user = User.builder()
                .id(userId)
                .firstName(newUser.firstName())
                .lastName(newUser.lastName())
                .username(newUser.username())
                .email(newUser.username())
                .phoneNumber(newUser.phoneNumber())
                .dateOfBirth(newUser.dateOfBirth())
                .income(newUser.income())
                .password(encoder.encode(newUser.password()))
                .build();

        User savedUser = userDao.save(user);

        if (savedUser.getId().equals((userId))){
            log.info("New user created in lottery-app-db database........");
        }

        if(!Objects.equals(201,response.getStatus())){

            throw new RuntimeException("Status code "+response.getStatus());
        }

        log.info("New user has bee created");

//        List<UserRepresentation> userRepresentations = usersResource.searchByUsername(newUser.username(), true);
//        UserRepresentation userRepresentation1 = userRepresentations.get(0);
//        sendVerificationEmail(userRepresentation1.getId());

        //SEND EMAIL BELOW THIS.........

        eventPublisher.publishEvent(new RegistrationCompleteEvent(user, applicationUrl(request)));
//        return new Response(company.getId(), 200, "Company created successfully, please complete the registration through the link.");


    }

    @Override
    public void sendVerificationEmail(String userId) {

        UsersResource usersResource = getUsersResource();
        usersResource.get(userId).sendVerifyEmail();
    }

    @Override
    public void deleteUser(String userId) {

        UsersResource usersResource = getUsersResource();
        usersResource.delete(userId);
    }

    @Override
    public void forgotPassword(String username) {
        UsersResource usersResource = getUsersResource();
        List<UserRepresentation> userRepresentations = usersResource.searchByUsername(username, true);
        UserRepresentation userRepresentation1 = userRepresentations.get(0);
        UserResource userResource = usersResource.get(userRepresentation1.getId());

        userResource.executeActionsEmail(List.of("UPDATE_PASSWORD"));
    }

    @Override
    public UserResource getUser(String userId) {
        UsersResource usersResource = getUsersResource();
        return usersResource.get(userId);
    }

    @Override
    public List<RoleRepresentation> getUserRoles(String userId) {


        return getUser(userId).roles().realmLevel().listAll();
    }

    @Override
    public List<GroupRepresentation> getUserGroups(String userId) {

        return getUser(userId).groups();
    }

    private UsersResource getUsersResource(){
        return keycloak.realm(realm).users();
    }



    public String verifyToken(String token, HttpServletRequest request) {

        VerificationToken theToken = verificationRepository.findByToken(token);
        if (theToken == null){
            return "<h4 style=\"margin-top: 400px;\"><center>Invalid verification token.</center></h4>";
        }
        User user = theToken.getUser();
        Calendar calendar = Calendar.getInstance();
        if ((theToken.getExpirationTime().getTime() - calendar.getTime().getTime()) <= 0){
            verificationRepository.delete(theToken);

            // TODO: add email resend logic here

            eventPublisher.publishEvent(new RegistrationCompleteEvent(user, applicationUrl(request)));

            return "<h4 style=\"margin-top: 400px;\"><center>Token is already expired!</center></h4>";
        }

        UserResource userResource = getUser(user.getId());
        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setEmailVerified(true);
        userResource.update(userRepresentation);

        user.setEnabled(true);
        user.setEmailVerified(true);

        userDao.save(user);
        return "<h4 style=\"margin-top: 400px;\"><center>Registration verified successfully.</center></h4>";
    }

    public void saveVerificationToken(User user, String token) {

        var verificationToken = new VerificationToken(token,user);

        verificationRepository.save(verificationToken);
    }

    public String applicationUrl(HttpServletRequest request) {

        return "http://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath();
    }

    @Override
    public User findUserById(String userId) {
        return userDao.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
    }
}
