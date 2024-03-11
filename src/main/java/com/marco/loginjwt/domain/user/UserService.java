package com.marco.loginjwt.domain.user;

import com.marco.loginjwt.api.user.UserRequest;
import com.marco.loginjwt.web.exception.ExceptionMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;

    public User create(UserRequest request) {
        List<User> user = userRepository.findByUsernameOrEmail(request.username(), request.email());
        if (user.size() > 0) throw new ExceptionMessage(HttpStatus.BAD_REQUEST, "username or e-mail in use.");
        String password = new BCryptPasswordEncoder().encode(request.password());
        User userToSave = new User(null, request.username(), request.email(), password);
        return userRepository.save(userToSave);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username);
    }
}
