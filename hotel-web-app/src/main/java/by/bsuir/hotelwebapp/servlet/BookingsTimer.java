package by.bsuir.hotelwebapp.servlet;

import java.util.List;
import java.util.TimerTask;

import org.hibernate.StatelessSession;

import by.bsuir.hotelwebapp.config.HibernateSession;
import by.bsuir.hotelwebapp.dto.response.BookingResponseDTO;
import by.bsuir.hotelwebapp.service.BookingService;
import by.bsuir.hotelwebapp.service.RoomTypeService;

public class BookingsTimer extends TimerTask {
    @Override
    public void run() {
        try (StatelessSession hibernateSession = HibernateSession.getStatelessSession()) {
            hibernateSession.beginTransaction();
            
            BookingService bookingService = new BookingService(hibernateSession);
            RoomTypeService roomTypeService = new RoomTypeService(hibernateSession);

            List<BookingResponseDTO> bookings = bookingService.getAllBooking();
            for (var booking : bookings) {
                bookingService.decrementNumberOfNights(booking.id());

                if (booking.numberOfNights() <= 1) {
                    roomTypeService.incrementFreeRooms(booking.roomTypeId());
                    bookingService.deleteBooking(booking.id());
                }
            }

            hibernateSession.getTransaction().commit();
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
