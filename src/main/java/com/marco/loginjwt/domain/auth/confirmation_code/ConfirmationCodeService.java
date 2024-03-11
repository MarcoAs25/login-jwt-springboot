package com.marco.loginjwt.domain.auth.confirmation_code;

import com.marco.loginjwt.api.user.UserConfirmationCodeRequest;
import com.marco.loginjwt.domain.user.User;
import com.marco.loginjwt.web.exception.ExceptionMessage;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class ConfirmationCodeService {
    @Value("${api.security.code.confirmation.expiration-seconds}")
    private Long codeConfirmationExpiration;

    private final ConfirmationCodeRepository confirmationCodeRepository;
    @Transactional
    public ConfirmationCode createConfirmationCode(User user) {
        deleteByUser(user); //delete old confirmation codes
        ConfirmationCode confirmationCode = new ConfirmationCode();
        confirmationCode = confirmationCode.builder()
                .user(user)
                .code(generateCode())
                .expiryDate(Instant.now().plusSeconds(codeConfirmationExpiration))
                .build();
        return confirmationCodeRepository.save(confirmationCode);
    }

    public String generateCode(){
        Random rand = new Random();
        String code = "";
        for(int i =0; i < 6; i++){
            Integer n = rand.nextInt(10);
            code+= n.toString();
        }
        return code;
    }

    public Optional<ConfirmationCode> findByEmail(String email) {
        return confirmationCodeRepository.findByUser_Email(email);
    }

    public void verify(UserConfirmationCodeRequest request) {
        ConfirmationCode confirmationCode = confirmationCodeRepository.findByUser_EmailAndCode(request.email(), request.code());
        if(confirmationCode != null){
            verifyExpiration(confirmationCode);
        }else{
            throw new ExceptionMessage(HttpStatus.BAD_REQUEST, "code confirmation invalid.");
        }
    }

    public ConfirmationCode verifyExpiration(ConfirmationCode token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            confirmationCodeRepository.delete(token);
            throw new ExceptionMessage(HttpStatus.BAD_REQUEST, "code confirmation is expired. Please send new email.");
        }
        return token;
    }
    private void deleteByUser(User user){
       Optional<ConfirmationCode> confirmationCode = confirmationCodeRepository.findByUser_Email(user.getEmail());
        confirmationCode.ifPresent(confirmationCodeRepository::delete);
    }
}
