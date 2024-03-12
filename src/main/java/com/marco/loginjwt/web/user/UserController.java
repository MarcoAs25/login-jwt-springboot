package com.marco.loginjwt.web.user;

import com.marco.loginjwt.api.auth.AuthRequest;
import com.marco.loginjwt.api.user.*;
import com.marco.loginjwt.domain.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    private ResponseEntity<UserResponse> create(@Valid @RequestBody UserRequest request) {
        UserResponse userResponse = UserMapper.toUserResponse(userService.create(request));
        return ResponseEntity.ok(userResponse);
    }

    @PostMapping("/confirm-register")
    private ResponseEntity<UserResponse> confirmRegister(@Valid @RequestBody UserConfirmationCodeRequest code) {
        userService.confirmRegister(code);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/forgot-password")
    private ResponseEntity<UserResponse> forgotPasswordRequest(@Valid @RequestBody ForgotPasswordRequest request) {
        userService.forgotPasswordRequest(request);
        return ResponseEntity.ok().build();
    }
    @GetMapping("/forgot-password/{code}/{email}")
    private ResponseEntity<UserPasswordTokenResponse> findTokenByEmailAndCode(@PathVariable("code") String code, @PathVariable("email") String email) {
        return ResponseEntity.ok(userService.findTokenByEmailAndCode(email, code));
    }
    @PostMapping("/forgot-password/reset-password")
    private ResponseEntity<UserPasswordTokenResponse> findTokenByEmailAndCode(@RequestHeader("Token-Password") String token, @Valid @RequestBody UserResetPasswordRequest request) {
        userService.resetPassword(token, request);
        return ResponseEntity.ok().build();
    }
}
