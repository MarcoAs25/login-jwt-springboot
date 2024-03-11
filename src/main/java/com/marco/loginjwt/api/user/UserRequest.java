package com.marco.loginjwt.api.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserRequest(@NotBlank(message = "inform the name.") String name,
                          @NotBlank(message = "inform the email.") @Email(message = "informe um email válido.") String email,
                          @NotBlank(message = "inform the password.") String password) {
}
