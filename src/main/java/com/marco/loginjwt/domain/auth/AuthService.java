package com.marco.loginjwt.domain.auth;

import com.marco.loginjwt.api.auth.AuthRequest;
import com.marco.loginjwt.api.auth.refresh_token.RefreshTokenRequest;
import com.marco.loginjwt.domain.auth.refresh_token.RefreshToken;
import com.marco.loginjwt.domain.auth.refresh_token.RefreshTokenService;
import com.marco.loginjwt.domain.user.User;
import com.marco.loginjwt.web.exception.ExceptionMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    public Map<String, String> auth(AuthRequest request) {
        Map<String, String> result = new HashMap<>();
        var usernamePassword = new UsernamePasswordAuthenticationToken(request.email(), request.password());
        var auth = authenticationManager.authenticate(usernamePassword);
        User user = (User) auth.getPrincipal();
        if (auth.isAuthenticated()) {
            var token = jwtService.generateToken(user);
            result.put("token", token);
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(request.email(), user);
            result.put("refresh", refreshToken.getToken());
            return result;
        }
        throw new ExceptionMessage(HttpStatus.UNAUTHORIZED, "invalid username or password");
    }

    public String authByRefreshToken(RefreshTokenRequest request) {
        return refreshTokenService.findByToken(request.token())
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String refreshToken = jwtService.generateToken(user);
                    return refreshToken;
                }).orElseThrow(() -> {
                    throw new ExceptionMessage(HttpStatus.UNAUTHORIZED, "refresh RefreshToken not valid.");
                });
    }
}
