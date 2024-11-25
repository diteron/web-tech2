package by.bsuir.hotelwebapp.service;

import java.util.List;

import org.hibernate.StatelessSession;

import by.bsuir.hotelwebapp.dto.BookingMapper;
import by.bsuir.hotelwebapp.dto.request.BookingRequestDTO;
import by.bsuir.hotelwebapp.dto.response.BookingResponseDTO;
import by.bsuir.hotelwebapp.entity.Booking;
import by.bsuir.hotelwebapp.entity.RoomType;
import by.bsuir.hotelwebapp.entity.User;
import by.bsuir.hotelwebapp.repository.BookingRepository;
import by.bsuir.hotelwebapp.repository.BookingRepository_;
import by.bsuir.hotelwebapp.repository.RoomTypeRepository;
import by.bsuir.hotelwebapp.repository.RoomTypeRepository_;
import by.bsuir.hotelwebapp.repository.UserRepository;
import by.bsuir.hotelwebapp.repository.UserRepository_;

public class BookingService {
    private final BookingRepository bookings;
    private final UserRepository users;
    private final RoomTypeRepository roomTypes;

    private static final BookingMapper BOOKING_MAPPER = new BookingMapper();

    public BookingService(StatelessSession session) {
        users = new UserRepository_(session);
        roomTypes = new RoomTypeRepository_(session);
        bookings = new BookingRepository_(session);
    }

    public void addBooking(BookingRequestDTO booking) {
        User user = users.findById(booking.userId()).orElseThrow();
        RoomType roomType = roomTypes.findById(booking.roomTypeId()).orElseThrow();
        Booking entity = BOOKING_MAPPER.toEntity(booking, user, roomType);

        bookings.save(entity);
    }   

    public List<BookingResponseDTO> getAllBooking() {
        return bookings.findAll().map(BOOKING_MAPPER::toResponseDTO).toList();
    }

    public void deleteBooking(Long id) {
        bookings.deleteById(id);
    }

    public void decrementNumberOfNights(Long bookingId) {
        Booking booking = bookings.findById(bookingId).orElseThrow();
        booking.decrementNumberOfNights();;
        bookings.update(booking);
    }
}
