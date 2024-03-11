package com.marco.loginjwt.web.user;

import com.marco.loginjwt.api.user.UserResponse;
import com.marco.loginjwt.domain.user.User;

public class UserMapper {
    public static UserResponse toUserResponse(User user) {
        return new UserResponse(user.getId(), user.getName(), user.getEmail());
    }
}
