package by.bsuir.hotelwebapp.dto.response;

public record UserResponseDTO(
    Long id,
    String username,
    String email,
    Boolean isAdmin
) {}
