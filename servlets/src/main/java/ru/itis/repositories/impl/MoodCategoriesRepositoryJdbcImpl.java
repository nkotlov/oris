package ru.itis.repositories.impl;

import ru.itis.models.MoodCategory;
import ru.itis.repositories.MoodCategoriesRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MoodCategoriesRepositoryJdbcImpl implements MoodCategoriesRepository {
    private final Connection connection;

    public MoodCategoriesRepositoryJdbcImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void save(MoodCategory moodCategory) {
        try (PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO mood_categories (name) VALUES (?)"
        )) {
            statement.setString(1, moodCategory.getName());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Error saving mood category", e);
        }
    }

    @Override
    public List<MoodCategory> findAll() {
        List<MoodCategory> categories = new ArrayList<>();
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM mood_categories");
            while (resultSet.next()) {
                categories.add(mapRowToMoodCategory(resultSet));
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Error finding all mood categories", e);
        }
        return categories;
    }

    @Override
    public void update(MoodCategory moodCategory) {
        try (PreparedStatement statement = connection.prepareStatement(
                "UPDATE mood_categories SET name = ? WHERE id = ?"
        )) {
            statement.setString(1, moodCategory.getName());
            statement.setLong(2, moodCategory.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Error updating mood category", e);
        }
    }

    @Override
    public void delete(Long id) {
        try (PreparedStatement statement = connection.prepareStatement(
                "DELETE FROM mood_categories WHERE id = ?"
        )) {
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Error deleting mood category", e);
        }
    }

    @Override
    public List<MoodCategory> findByName(String name) {
        List<MoodCategory> categories = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(
                "SELECT * FROM mood_categories WHERE name ILIKE ?"
        )) {
            statement.setString(1, "%" + name + "%");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                categories.add(mapRowToMoodCategory(resultSet));
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Error finding mood categories by name", e);
        }
        return categories;
    }

    private MoodCategory mapRowToMoodCategory(ResultSet resultSet) throws SQLException {
        return MoodCategory.builder()
                .id(resultSet.getLong("id"))
                .name(resultSet.getString("name"))
                .build();
    }
}
