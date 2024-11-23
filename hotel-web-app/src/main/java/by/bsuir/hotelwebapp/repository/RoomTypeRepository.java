package by.bsuir.hotelwebapp.repository;

import java.util.Optional;

import by.bsuir.hotelwebapp.entity.RoomType;
import jakarta.data.repository.CrudRepository;
import jakarta.data.repository.Find;

public interface RoomTypeRepository extends CrudRepository<RoomType, Long> {
    @Find
    Optional<RoomType> getByName(String name);
}
