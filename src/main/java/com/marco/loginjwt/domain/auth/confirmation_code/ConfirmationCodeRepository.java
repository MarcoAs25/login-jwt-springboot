package com.marco.loginjwt.domain.auth.confirmation_code;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConfirmationCodeRepository extends JpaRepository<ConfirmationCode, Long> {
    Optional<ConfirmationCode> findByUser_Email(String email);

    ConfirmationCode findByUser_EmailAndCode(String email, String code);
}
