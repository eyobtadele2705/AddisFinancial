package com.programming.keycloak.repository;
import com.programming.keycloak.model.token.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenVerificationRepository extends JpaRepository<VerificationToken, Long> {

    VerificationToken findByToken(String token);
}
