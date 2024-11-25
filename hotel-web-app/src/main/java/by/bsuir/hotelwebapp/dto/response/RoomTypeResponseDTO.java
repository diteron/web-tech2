package by.bsuir.hotelwebapp.dto.response;

public record RoomTypeResponseDTO(
    Long id,
    String name,
    Integer price,
    Integer freeRooms
) {}
