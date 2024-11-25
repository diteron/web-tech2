package by.bsuir.hotelwebapp.repository;

import java.util.Optional;
import java.util.stream.Stream;

import by.bsuir.hotelwebapp.entity.Booking;
import jakarta.data.repository.Delete;
import jakarta.data.repository.Find;
import jakarta.data.repository.Repository;
import jakarta.data.repository.Save;
import jakarta.data.repository.Update;

@Repository
public interface BookingRepository {
    @Find
    Optional<Booking> findById(Long id);

    @Save
    Booking save(Booking booking);

    @Find
    Stream<Booking> findAll();

    @Delete
    void deleteById(Long id);

    @Update
    Booking update(Booking booking);
}
