package ru.itis.repositories.impl;

import ru.itis.models.User;
import ru.itis.repositories.UsersRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UsersRepositoryJdbcImpl implements UsersRepository {
    private final Connection connection;

    public UsersRepositoryJdbcImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void save(User user) {
        try (PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO users (first_name, last_name, email, password) VALUES (?, ?, ?, ?)"
        )) {
            statement.setString(1, user.getFirstName());
            statement.setString(2, user.getLastName());
            statement.setString(3, user.getEmail());
            statement.setString(4, user.getPassword());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Error saving user", e);
        }
    }
    
    @Override
    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM users");
            while (resultSet.next()) {
                users.add(mapRowToUser(resultSet));
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Error finding all users", e);
        }
        return users;
    }

    private User mapRowToUser(ResultSet resultSet) throws SQLException {
        return User.builder()
                .id(resultSet.getLong("id"))
                .firstName(resultSet.getString("first_name"))
                .lastName(resultSet.getString("last_name"))
                .email(resultSet.getString("email"))
                .password(resultSet.getString("password"))
                .build();
    }
}
