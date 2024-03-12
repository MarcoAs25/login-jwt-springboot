package com.marco.loginjwt.domain.user;

import com.marco.loginjwt.api.user.*;
import com.marco.loginjwt.domain.auth.confirmation_code.CodeType;
import com.marco.loginjwt.domain.auth.confirmation_code.ConfirmationCode;
import com.marco.loginjwt.domain.auth.confirmation_code.ConfirmationCodeService;
import com.marco.loginjwt.domain.auth.confirmation_code.VerificationStrategy;
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
        confirmationCodeService.verify(code, CodeType.ACCOUNT_CONFIRMATION, VerificationStrategy.CODE);
        int rowsAffected = userRepository.updateActivatedByEmail(true, code.email());
        if(rowsAffected == 0){
            throw new ExceptionMessage(HttpStatus.BAD_REQUEST, "Account not confirmed.");
        }
    }

    public void forgotPasswordRequest(ForgotPasswordRequest request) {
        User user = userRepository.findByEmail(request.email());
        if(user == null) throw new ExceptionMessage(HttpStatus.NOT_FOUND, "Usuário não encontrado");
        emailService.sendCodeResetPassword(user);
    }

    public UserPasswordTokenResponse findTokenByEmailAndCode(String email, String code) {
        ConfirmationCode confirmationCode = confirmationCodeService.verify(new UserConfirmationCodeRequest(email, code, null), CodeType.RESET_PASSWORD, VerificationStrategy.CODE);
        return new UserPasswordTokenResponse(confirmationCode.getToken());
    }

    public void resetPassword(String token, UserResetPasswordRequest request) {
        User user = userRepository.findByEmail(request.email());
        confirmationCodeService.verify(new UserConfirmationCodeRequest(request.email(), null, token), CodeType.RESET_PASSWORD, VerificationStrategy.TOKEN);
        String password = new BCryptPasswordEncoder().encode(request.password());
        user.setPassword(password);
        userRepository.save(user);
    }
}
