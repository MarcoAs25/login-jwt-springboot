package com.marco.loginjwt.web.auth;

import com.marco.loginjwt.api.auth.AuthRequest;
import com.marco.loginjwt.api.auth.AuthResponse;
import com.marco.loginjwt.api.auth.refresh_token.RefreshTokenRequest;
import com.marco.loginjwt.api.auth.refresh_token.RefreshTokenResponse;
import com.marco.loginjwt.domain.auth.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping
    private ResponseEntity<AuthResponse> authorize(@Valid @RequestBody AuthRequest request) {
        AuthResponse response = AuthMapper.toAuthResponse(authService.auth(request));
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh-token")
    private ResponseEntity<RefreshTokenResponse> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        RefreshTokenResponse response = new RefreshTokenResponse(authService.authByRefreshToken(request));
        return ResponseEntity.ok(response);
    }
}
