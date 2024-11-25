package by.bsuir.hotelwebapp.dto.request;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;

public record BookingRequestDTO(
    Long id,

    @NotNull
    Long userId,

    @NotNull
    Long roomTypeId,

    @NotNull
    Integer nuberOfNights,

    @NotNull
    LocalDateTime bookingDate
) {}
