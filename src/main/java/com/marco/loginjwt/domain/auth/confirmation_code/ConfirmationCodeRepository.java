package com.marco.loginjwt.domain.auth.confirmation_code;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConfirmationCodeRepository extends JpaRepository<ConfirmationCode, Long> {
    Optional<ConfirmationCode> findByUser_EmailAndCodeType(String email, CodeType codeType);

    ConfirmationCode findByUser_EmailAndCodeAndCodeType(String email, String code, CodeType codeType);

    ConfirmationCode findByUser_EmailAndTokenAndCodeType(String email, String code, CodeType codeType);
}
