package com.marco.loginjwt.web.user;

import com.marco.loginjwt.api.user.UserRequest;
import com.marco.loginjwt.api.user.UserResponse;
import com.marco.loginjwt.domain.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
