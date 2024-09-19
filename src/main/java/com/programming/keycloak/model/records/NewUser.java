package com.programming.keycloak.model.records;

public record NewUser(
        String username,
        String password,
        String email,
        String phoneNumber,
        String firstName,
        String lastName,
        String dateOfBirth,
        Double income
) {
}
