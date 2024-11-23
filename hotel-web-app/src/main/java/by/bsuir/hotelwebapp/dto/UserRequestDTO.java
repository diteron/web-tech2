package by.bsuir.hotelwebapp.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UserRequestDTO (
    @NotNull
    @Size(min = 2, max = 64, message = "Login must be between 2 and 64 characters")
    String username,

    @NotNull
    @Size(min = 8, max = 128, message = "Password must be between 8 and 128 characters")
    String password,

    @Email(message = "Incorrect email")
    String email,
    
    @NotNull
    Boolean isAdmin    
) {}
