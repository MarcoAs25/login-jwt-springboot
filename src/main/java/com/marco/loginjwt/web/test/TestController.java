package com.marco.loginjwt.web.test;

import com.marco.loginjwt.api.user.UserResponse;
import com.marco.loginjwt.domain.user.User;
import com.marco.loginjwt.web.user.UserMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class TestController {
    @GetMapping
    public ResponseEntity<UserResponse> teste() {
        UserResponse response = UserMapper.toUserResponse((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        return ResponseEntity.ok(response);
    }
}
