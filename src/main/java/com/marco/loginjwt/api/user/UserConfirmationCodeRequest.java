package com.marco.loginjwt.api.user;

import com.marco.loginjwt.domain.auth.confirmation_code.CodeType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserConfirmationCodeRequest(@NotBlank(message = "inform the email.") @Email(message = "invalid email.") String email, @NotBlank(message = "inform the code.") String code, String token) {
}
