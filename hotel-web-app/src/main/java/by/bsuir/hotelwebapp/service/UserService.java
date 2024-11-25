package by.bsuir.hotelwebapp.service;

import java.util.Optional;

import org.hibernate.StatelessSession;

import by.bsuir.hotelwebapp.dto.request.UserRequestDTO;
import by.bsuir.hotelwebapp.dto.response.UserResponseDTO;
import by.bsuir.hotelwebapp.entity.User;
import by.bsuir.hotelwebapp.repository.UserRepository;
import by.bsuir.hotelwebapp.repository.UserRepository_;
import by.bsuir.hotelwebapp.service.security.PasswordHasher;
import jakarta.persistence.EntityNotFoundException;

public class UserService {
    private final UserRepository users;

    public UserService(StatelessSession session) {
        users = new UserRepository_(session);
    }

    public UserResponseDTO getUserById(Long id) {
        User user = users.findById(id).orElseThrow();
        return new UserResponseDTO(user.getId(), user.getUsername(), user.getEmail(), user.isAdmin());
    }

    public UserResponseDTO getUserByUsername(String username) {
        User user = users.findByUsername(username).orElseThrow(() -> 
                new EntityNotFoundException("User with username " + username + " not found"));
        return new UserResponseDTO(user.getId(), user.getUsername(), user.getEmail(), user.isAdmin());
    }

    public boolean isUserExist(String username) {
        Optional<User> foundUser = users.findByUsername(username);
        return foundUser.isPresent();
    }

    public boolean addUser(UserRequestDTO user) {
        if (!isUserExist(user.username())) {
            String passwordHash = PasswordHasher.getPasswordHash(user.password());
            users.save(new User(null, user.username(), passwordHash, user.email(), user.isAdmin()));
            return true;
        }
        else {
            return false;
        }
    }

    public boolean isUserAuthorized(UserRequestDTO user) {
        Optional<User> foundUser = users.findByUsername(user.username());

        if (foundUser.isPresent()) {
            User userEntity = foundUser.get();
            String storedPasswordHash = userEntity.getPasswordHash();
            String enteredPassword = user.password();

            return PasswordHasher.checkPasswordAgainstHash(enteredPassword, storedPasswordHash);
        }
        else {
            return false;
        }
    }
}
