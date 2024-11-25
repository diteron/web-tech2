package by.bsuir.hotelwebapp.repository;

import java.util.Optional;
import java.util.stream.Stream;

import by.bsuir.hotelwebapp.entity.RoomType;
import jakarta.data.repository.Find;
import jakarta.data.repository.OrderBy;
import jakarta.data.repository.Repository;
import jakarta.data.repository.Update;

@Repository
public interface RoomTypeRepository {
    @Find
    Optional<RoomType> findById(Long id);

    @Find
    Optional<RoomType> findByName(String name);

    @Find
    @OrderBy("price")
    Stream<RoomType> findAll();

    @Update
    RoomType update(RoomType roomType);
}
