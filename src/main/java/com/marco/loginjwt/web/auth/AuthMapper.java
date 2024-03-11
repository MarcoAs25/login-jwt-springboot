package com.marco.loginjwt.web.auth;

import com.marco.loginjwt.api.auth.AuthResponse;

import java.util.Map;

public class AuthMapper {
    public static AuthResponse toAuthResponse(Map<String, String> auth) {
        return new AuthResponse(auth.get("token"), auth.get("refresh"));
    }
}
