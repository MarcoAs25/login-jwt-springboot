package com.marco.loginjwt.api.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ForgotPasswordRequest(@NotBlank(message = "inform the email.") @Email(message = "invalid email.") String email) {
}
