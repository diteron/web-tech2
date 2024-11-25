package by.bsuir.hotelwebapp.repository;

import java.util.Optional;

import by.bsuir.hotelwebapp.entity.User;
import jakarta.data.repository.Find;
import jakarta.data.repository.Repository;
import jakarta.data.repository.Save;

@Repository
public interface UserRepository {
    @Find
    Optional<User> findById(Long id);

    @Find
    Optional<User> findByUsername(String username);

    @Save
    User save(User user);
}
