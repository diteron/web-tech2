package by.bsuir.hotelwebapp.dto;

import by.bsuir.hotelwebapp.dto.request.RoomTypeRequestDTO;
import by.bsuir.hotelwebapp.dto.response.RoomTypeResponseDTO;
import by.bsuir.hotelwebapp.entity.RoomType;

public class RoomTypeMapper {
    public RoomTypeResponseDTO toResponseDTO(RoomType entity) {
        return new RoomTypeResponseDTO(entity.getId(), entity.getName(), entity.getPrice(),
                entity.getFreeRooms());
    }

    public RoomType toEntity(RoomTypeRequestDTO requestDTO) {
        return new RoomType(requestDTO.id(), requestDTO.name(), requestDTO.price(), requestDTO.freeRooms());
    }
}
