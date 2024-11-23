package by.bsuir.hotelwebapp.dto;

public record UserResponseDTO(
    String username,
    String email,
    Boolean isAdmin
) {}
