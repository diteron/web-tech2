package by.bsuir.hotelwebapp.dto.response;

import java.time.LocalDateTime;

public record BookingResponseDTO(
    Long id,
    Long userId,
    Long roomTypeId,
    Integer numberOfNights,
    LocalDateTime bookingDate
) {}
