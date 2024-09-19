package com.programming.keycloak.repository;

import com.programming.keycloak.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDao extends JpaRepository<User, String> {
}
