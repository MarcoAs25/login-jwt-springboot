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
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ConfirmationCodeService {
    @Value("${api.security.code.confirmation.expiration-seconds}")
    private Long codeConfirmationExpiration;

    private final ConfirmationCodeRepository confirmationCodeRepository;
    @Transactional
    public ConfirmationCode createConfirmationCode(User user, CodeType codeType) {
        deleteByUser(user, codeType);
        ConfirmationCode confirmationCode = new ConfirmationCode();
        confirmationCode = confirmationCode.builder()
                .user(user)
                .code(generateCode())
                .token(codeType == CodeType.ACCOUNT_CONFIRMATION? null: UUID.randomUUID().toString())
                .expiryDate(Instant.now().plusSeconds(codeConfirmationExpiration))
                .codeType(codeType)
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

    public Optional<ConfirmationCode> findByEmail(String email, CodeType codeType) {
        return confirmationCodeRepository.findByUser_EmailAndCodeType(email, codeType);
    }

    public ConfirmationCode verify(UserConfirmationCodeRequest request, CodeType codeType, VerificationStrategy verificationStrategy) {
        ConfirmationCode confirmationCode;
        switch (verificationStrategy){
            case CODE -> confirmationCode = confirmationCodeRepository.findByUser_EmailAndCodeAndCodeType(request.email(), request.code(), codeType);
            case TOKEN -> confirmationCode = confirmationCodeRepository.findByUser_EmailAndTokenAndCodeType(request.email(), request.token(), codeType);
            default -> throw new ExceptionMessage(HttpStatus.BAD_REQUEST, "verification strategy not found.");
        }
        if(confirmationCode != null){
            verifyExpiration(confirmationCode);
        }else{
            throw new ExceptionMessage(HttpStatus.BAD_REQUEST, "code confirmation invalid.");
        }
        return confirmationCode;
    }

    public ConfirmationCode verifyExpiration(ConfirmationCode token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            confirmationCodeRepository.delete(token);
            throw new ExceptionMessage(HttpStatus.BAD_REQUEST, "code confirmation is expired. Please send new email.");
        }
        return token;
    }
    private void deleteByUser(User user, CodeType codeType){
       Optional<ConfirmationCode> confirmationCode = findByEmail(user.getEmail(), codeType);
        confirmationCode.ifPresent(confirmationCodeRepository::delete);
    }
}
