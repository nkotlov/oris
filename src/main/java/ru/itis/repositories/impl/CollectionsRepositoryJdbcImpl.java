package ru.itis.repositories.impl;

import ru.itis.models.Collection;
import ru.itis.repositories.CollectionsRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CollectionsRepositoryJdbcImpl implements CollectionsRepository {
    private final Connection connection;

    public CollectionsRepositoryJdbcImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void save(Collection collection) {
        try (PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO collections (name, description, user_id) VALUES (?, ?, ?)"
        )) {
            statement.setString(1, collection.getName());
            statement.setString(2, collection.getDescription());
            statement.setLong(3, collection.getUser().getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Error saving collection", e);
        }
    }

    @Override
    public List<Collection> findAll() {
        List<Collection> collections = new ArrayList<>();
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM collections");
            while (resultSet.next()) {
                collections.add(mapRowToCollection(resultSet));
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Error finding all collections", e);
        }
        return collections;
    }

    @Override
    public void update(Collection collection) {
        try (PreparedStatement statement = connection.prepareStatement(
                "UPDATE collections SET name = ?, description = ? WHERE id = ?"
        )) {
            statement.setString(1, collection.getName());
            statement.setString(2, collection.getDescription());
            statement.setLong(3, collection.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Error updating collection", e);
        }
    }

    @Override
    public void delete(Long id) {
        try (PreparedStatement statement = connection.prepareStatement(
                "DELETE FROM collections WHERE id = ?"
        )) {
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Error deleting collection", e);
        }
    }

    @Override
    public List<Collection> findByName(String name) {
        List<Collection> collections = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(
                "SELECT * FROM collections WHERE name ILIKE ?"
        )) {
            statement.setString(1, "%" + name + "%");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                collections.add(mapRowToCollection(resultSet));
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Error finding collections by name", e);
        }
        return collections;
    }

    @Override
    public List<Collection> findByUserId(Long userId) {
        List<Collection> collections = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(
                "SELECT * FROM collections WHERE user_id = ?"
        )) {
            statement.setLong(1, userId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                collections.add(mapRowToCollection(resultSet));
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Error finding collections by user ID", e);
        }
        return collections;
    }

    private Collection mapRowToCollection(ResultSet resultSet) throws SQLException {
        return Collection.builder()
                .id(resultSet.getLong("id"))
                .name(resultSet.getString("name"))
                .description(resultSet.getString("description"))
                .build();
    }
}
