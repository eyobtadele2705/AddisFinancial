package com.programming.keycloak.api;

import com.programming.keycloak.model.records.NewUser;
import com.programming.keycloak.model.token.VerificationToken;
import com.programming.keycloak.repository.TokenVerificationRepository;
import com.programming.keycloak.service.impl.UserServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserServiceImpl userService;
    private final TokenVerificationRepository verificationRepository;


    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody NewUser newUser, final HttpServletRequest request) {

        userService.createUser(newUser, request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    @PutMapping("/{userId}/send-verification-email")
    public ResponseEntity<?> sendVerificationEmail(
            @PathVariable("userId") String userId) {

        userService.sendVerificationEmail(userId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
    @PutMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(
            @RequestParam String username) {

        userService.forgotPassword(username);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteUser(
            @PathVariable("userId") String userId) {

        userService.deleteUser(userId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
    @GetMapping("/{id}/roles")
    public ResponseEntity<?> getUserRoles(@PathVariable String id) {

        return ResponseEntity.status(HttpStatus.OK).body(userService.getUserRoles(id));
    }

    @GetMapping("/{id}/groups")
    public ResponseEntity<?> getUserGroups(@PathVariable String id) {

        return ResponseEntity.status(HttpStatus.OK).body(userService.getUserGroups(id));
    }

    @GetMapping("/verifyEmail")
    public String verifyEmail(@RequestParam String token, final HttpServletRequest request){
        VerificationToken theToken = verificationRepository.findByToken(token);

        if (theToken.getUser().isEmailVerified()){
            return "<h4 style=\"margin-top: 400px;\"><center>This account has been already verified.</center></h4>";

        }

        return userService.verifyToken(token, request);
//        String verificationResult = userService.verifyToken(token);
    }
}
