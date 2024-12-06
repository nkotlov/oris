package ru.itis.repositories;

import ru.itis.models.User;
import java.util.List;

public interface UsersRepository {
    void save(User user);
    List<User> findAll();
}
