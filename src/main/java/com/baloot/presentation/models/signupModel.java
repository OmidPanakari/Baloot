package com.baloot.presentation.models;

public record signupModel(String username, String password, String confirmPassword, String email,
                          String birthDate, String address) {
}
