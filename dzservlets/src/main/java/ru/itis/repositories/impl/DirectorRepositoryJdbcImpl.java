package ru.itis.repositories.impl;

import ru.itis.models.Director;
import ru.itis.models.Movie;
import ru.itis.repositories.DirectorRepository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DirectorRepositoryJdbcImpl implements DirectorRepository {
    private final DataSource dataSource;

    public DirectorRepositoryJdbcImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void save(Director entity) {
        String sql = "INSERT INTO directors (name, bio, birth_date, photo_file_id) VALUES (?, ?, ?, ?)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, entity.getName());
            statement.setString(2, entity.getBio());
            statement.setDate(3, entity.getBirthDate() != null ? Date.valueOf(entity.getBirthDate()) : null);
            if (entity.getPhotoFileId() != null) {
                statement.setLong(4, entity.getPhotoFileId());
            } else {
                statement.setNull(4, Types.BIGINT);
            }
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
    public Optional<Director> findById(Long id) {
        String sql = "SELECT * FROM directors WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(mapRowToDirector(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    @Override
    public List<Director> findAll() {
        String sql = "SELECT * FROM directors";
        List<Director> directors = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                directors.add(mapRowToDirector(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return directors;
    }

    @Override
    public void update(Director entity) {
        String sql = "UPDATE directors SET name = ?, bio = ?, birth_date = ?, photo_file_id = ? WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, entity.getName());
            statement.setString(2, entity.getBio());
            statement.setDate(3, entity.getBirthDate() != null ? Date.valueOf(entity.getBirthDate()) : null);
            if (entity.getPhotoFileId() != null) {
                statement.setLong(4, entity.getPhotoFileId());
            } else {
                statement.setNull(4, Types.BIGINT);
            }
            statement.setLong(5, entity.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM directors WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Movie> findMoviesByDirectorId(Long directorId) {
        String sql = "SELECT * FROM movies WHERE director_id = ?";
        List<Movie> movies = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, directorId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                movies.add(mapRowToMovie(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return movies;
    }

    private Director mapRowToDirector(ResultSet resultSet) throws SQLException {
        return Director.builder()
                .id(resultSet.getLong("id"))
                .name(resultSet.getString("name"))
                .bio(resultSet.getString("bio"))
                .birthDate(resultSet.getDate("birth_date") != null
                        ? resultSet.getDate("birth_date").toLocalDate()
                        : null)
                .photoFileId(resultSet.getObject("photo_file_id", Long.class))
                .build();
    }

    private Movie mapRowToMovie(ResultSet resultSet) throws SQLException {
        return Movie.builder()
                .id(resultSet.getLong("id"))
                .title(resultSet.getString("title"))
                .description(resultSet.getString("description"))
                .releaseYear(resultSet.getInt("release_year"))
                .directorId(resultSet.getObject("director_id", Long.class))
                .fileId(resultSet.getObject("file_id", Long.class))
                .build();
    }
}
