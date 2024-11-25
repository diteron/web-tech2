package by.bsuir.hotelwebapp.dto.request;

import jakarta.validation.constraints.NotNull;

public record RoomTypeRequestDTO(
    Long id,

    @NotNull
    String name,

    @NotNull
    Integer price,

    @NotNull
    Integer freeRooms
) {}
