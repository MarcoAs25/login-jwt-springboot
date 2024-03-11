package com.marco.loginjwt.domain.user;

import com.marco.loginjwt.api.user.UserConfirmationCodeRequest;
import com.marco.loginjwt.api.user.UserRequest;
import com.marco.loginjwt.domain.auth.confirmation_code.ConfirmationCodeService;
import com.marco.loginjwt.domain.email.EmailService;
import com.marco.loginjwt.web.exception.ExceptionMessage;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final ConfirmationCodeService confirmationCodeService;

    @Transactional
    public User create(UserRequest request) {

        User userToSave = new User();
        UserDetails userDetails = loadUserByUsername(request.email());

        if(userDetails != null && !userDetails.isEnabled()){
            userToSave = (User) userDetails;
        }else{
            if(userDetails != null) throw new ExceptionMessage("E-mail in use.");
        }

        String password = new BCryptPasswordEncoder().encode(request.password());

        userToSave.setName(request.name());
        userToSave.setEmail(request.email());
        userToSave.setPassword(password);
        userToSave.setActivated(false);
        userToSave = userRepository.save(userToSave);

        emailService.sendMailConfirmation(userToSave);
        return userToSave;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email);
    }
    @Transactional
    public void confirmRegister(UserConfirmationCodeRequest code) {
        confirmationCodeService.verify(code);
        int rowsAffected = userRepository.updateActivatedByEmail(true, code.email());
        if(rowsAffected == 0){
            throw new ExceptionMessage(HttpStatus.BAD_REQUEST, "Account not confirmed.");
        }
    }
}
