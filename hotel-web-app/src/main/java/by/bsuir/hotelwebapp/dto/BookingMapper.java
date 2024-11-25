package by.bsuir.hotelwebapp.dto;

import by.bsuir.hotelwebapp.dto.request.BookingRequestDTO;
import by.bsuir.hotelwebapp.dto.response.BookingResponseDTO;
import by.bsuir.hotelwebapp.entity.Booking;
import by.bsuir.hotelwebapp.entity.RoomType;
import by.bsuir.hotelwebapp.entity.User;

public class BookingMapper {
    public BookingResponseDTO toResponseDTO(Booking entity) {
        return new BookingResponseDTO(entity.getId(), entity.getUser().getId(),
                entity.getRoomType().getId(), entity.getNumberOfNights(), entity.getBookingDate());
    }

    public Booking toEntity(BookingRequestDTO requestDTO, User user, RoomType roomType) {
        return new Booking(requestDTO.id(), user, roomType,
                requestDTO.nuberOfNights(), requestDTO.bookingDate());
    }
}

