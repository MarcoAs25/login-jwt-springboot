package com.marco.loginjwt.api.user;

public record UserResetPasswordRequest(String email, String password) {
}
