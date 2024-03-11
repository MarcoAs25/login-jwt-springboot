package com.marco.loginjwt.api.user;

import jakarta.validation.constraints.NotBlank;

public record UserConfirmationCodeRequest(@NotBlank(message = "inform the email.") String email, @NotBlank(message = "inform the code.") String code) {
}
