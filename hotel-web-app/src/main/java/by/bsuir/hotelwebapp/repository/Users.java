package by.bsuir.hotelwebapp.repository;

import java.util.Optional;

import by.bsuir.hotelwebapp.entity.User;
import jakarta.data.repository.CrudRepository;
import jakarta.data.repository.Find;
import jakarta.data.repository.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    @Find
    Optional<User> findByUsername(String username);
}
