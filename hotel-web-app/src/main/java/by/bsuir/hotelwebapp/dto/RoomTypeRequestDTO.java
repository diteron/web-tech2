package by.bsuir.hotelwebapp.dto;

import jakarta.validation.constraints.NotNull;

public record RoomTypeRequestDTO(
    @NotNull
    String name
) {}
