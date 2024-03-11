package com.marco.loginjwt.api.auth;

import jakarta.validation.constraints.NotBlank;

public record AuthRequest(@NotBlank(message = "inform the email.") String email,
                          @NotBlank(message = "inform the password.") String password) {
}
