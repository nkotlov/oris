package ru.itis.repositories;

import ru.itis.models.Movie;
import ru.itis.models.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends CrudRepository<User> {
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
    List<Movie> findMoviesByUserId(Long userId);
    void addMovieToUser(Long userId, Long movieId, String status);
    void removeMovieFromUser(Long userId, Long movieId);
    void updateMovieStatus(Long userId, Long movieId, String newStatus);
}
