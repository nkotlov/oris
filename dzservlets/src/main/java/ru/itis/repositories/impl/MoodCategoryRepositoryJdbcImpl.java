package ru.itis.repositories.impl;

import ru.itis.models.MoodCategory;
import ru.itis.repositories.MoodCategoryRepository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MoodCategoryRepositoryJdbcImpl implements MoodCategoryRepository {
    private final DataSource dataSource;

    public MoodCategoryRepositoryJdbcImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void save(MoodCategory entity) {
        String sql = "INSERT INTO mood_categories (name) VALUES (?)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, entity.getName());
            statement.executeUpdate();

            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                entity.setId(generatedKeys.getLong(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<MoodCategory> findById(Long id) {
        String sql = "SELECT * FROM mood_categories WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(mapRowToMoodCategory(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    @Override
    public List<MoodCategory> findAll() {
        String sql = "SELECT * FROM mood_categories";
        List<MoodCategory> categories = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                categories.add(mapRowToMoodCategory(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return categories;
    }

    @Override
    public void update(MoodCategory entity) {
        String sql = "UPDATE mood_categories SET name = ? WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, entity.getName());
            statement.setLong(2, entity.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM mood_categories WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Long> findPlaylistsByCategoryId(Long categoryId) {
        String sql = "SELECT playlist_id FROM mood_playlists WHERE category_id = ?";
        List<Long> playlistIds = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, categoryId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                playlistIds.add(resultSet.getLong("playlist_id"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return playlistIds;
    }

    @Override
    public void addPlaylistToCategory(Long categoryId, Long playlistId) {
        String sql = "UPDATE mood_playlists SET category_id = ? WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, categoryId);
            statement.setLong(2, playlistId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void removePlaylistFromCategory(Long categoryId, Long playlistId) {
        String sql = "UPDATE mood_playlists SET category_id = NULL WHERE id = ? AND category_id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, playlistId);
            statement.setLong(2, categoryId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private MoodCategory mapRowToMoodCategory(ResultSet resultSet) throws SQLException {
        return MoodCategory.builder()
                .id(resultSet.getLong("id"))
                .name(resultSet.getString("name"))
                .build();
    }
}
