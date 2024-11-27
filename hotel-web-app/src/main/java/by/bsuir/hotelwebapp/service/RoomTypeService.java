package by.bsuir.hotelwebapp.service;

import java.util.List;

import org.hibernate.StatelessSession;

import by.bsuir.hotelwebapp.dto.RoomTypeMapper;
import by.bsuir.hotelwebapp.dto.request.RoomTypeRequestDTO;
import by.bsuir.hotelwebapp.dto.response.RoomTypeResponseDTO;
import by.bsuir.hotelwebapp.entity.RoomType;
import by.bsuir.hotelwebapp.repository.RoomTypeRepository;
import by.bsuir.hotelwebapp.repository.RoomTypeRepository_;

public class RoomTypeService {
    private final RoomTypeRepository roomTypes;

    private final static RoomTypeMapper ROOM_TYPE_MAPPER = new RoomTypeMapper();

    public RoomTypeService(StatelessSession session) {
        roomTypes = new RoomTypeRepository_(session);
    }

    public RoomTypeResponseDTO getRoomTypeById(Long id) {
        RoomType roomType = roomTypes.findById(id).orElseThrow();
        return new RoomTypeResponseDTO(roomType.getId(), roomType.getName(),
                roomType.getPrice(), roomType.getFreeRooms());
    }

    public RoomTypeResponseDTO getRoomTypeByName(String name) {
        RoomType roomType = roomTypes.findByName(name).orElseThrow();
        return new RoomTypeResponseDTO(roomType.getId(), roomType.getName(),
                roomType.getPrice(), roomType.getFreeRooms());
    }

    public List<RoomTypeResponseDTO> getAvailableRooms() {
        return roomTypes.findAvailableRooms().map(ROOM_TYPE_MAPPER::toResponseDTO).toList();
    }

    public List<RoomTypeResponseDTO> getAllRoomTypes() {
        return roomTypes.findAll().map(ROOM_TYPE_MAPPER::toResponseDTO).toList();
    }

    public RoomTypeResponseDTO updateRoomType(RoomTypeRequestDTO roomType) {
        RoomType updateEntity = roomTypes.findById(roomType.id()).orElseThrow();
        updateEntity.setPrice(roomType.price());
        updateEntity.setFreeRooms(roomType.freeRooms());

        return ROOM_TYPE_MAPPER.toResponseDTO(roomTypes.update(updateEntity));
    }

    public void decrementFreeRooms(Long roomTypeId) {
        RoomType roomType = roomTypes.findById(roomTypeId).orElseThrow();
        roomType.decrementFreeRooms();
        roomTypes.update(roomType);
    }

    public void incrementFreeRooms(Long roomTypeId) {
        RoomType roomType = roomTypes.findById(roomTypeId).orElseThrow();
        roomType.incrementFreeRooms();
        roomTypes.update(roomType);
    }
}
