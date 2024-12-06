package ru.itis.repositories.impl;

import ru.itis.models.Movie;
import ru.itis.repositories.MoviesRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MoviesRepositoryJdbcImpl implements MoviesRepository {
    private final Connection connection;

    public MoviesRepositoryJdbcImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<Movie> findByCollectionId(Long collectionId) {
        List<Movie> movies = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(
                "SELECT m.* FROM movies m " +
                        "JOIN collections_movies cm ON m.id = cm.movie_id " +
                        "WHERE cm.collection_id = ?"
        )) {
            statement.setLong(1, collectionId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                movies.add(mapRowToMovie(resultSet));
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Error finding movies by collection ID", e);
        }
        return movies;
    }

    @Override
    public List<Movie> findByMoodCategoryId(Long moodCategoryId) {
        List<Movie> movies = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(
                "SELECT m.* FROM movies m " +
                        "JOIN moods_movies mm ON m.id = mm.movie_id " +
                        "WHERE mm.mood_category_id = ?"
        )) {
            statement.setLong(1, moodCategoryId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                movies.add(mapRowToMovie(resultSet));
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Error finding movies by mood category ID", e);
        }
        return movies;
    }

    @Override
    public List<Movie> findAll() {
        List<Movie> movies = new ArrayList<>();
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM movies");
            while (resultSet.next()) {
                movies.add(mapRowToMovie(resultSet));
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Error finding all movies", e);
        }
        return movies;
    }

    @Override
    public void save(Movie movie) {
        try (PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO movies (title, description, release_year) VALUES (?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS
        )) {
            statement.setString(1, movie.getTitle());
            statement.setString(2, movie.getDescription());
            statement.setInt(3, movie.getReleaseYear());
            statement.executeUpdate();

            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                movie.setId(generatedKeys.getLong(1));
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Error saving movie", e);
        }
    }

    @Override
    public void update(Movie movie) {
        try (PreparedStatement statement = connection.prepareStatement(
                "UPDATE movies SET title = ?, description = ?, release_year = ? WHERE id = ?"
        )) {
            statement.setString(1, movie.getTitle());
            statement.setString(2, movie.getDescription());
            statement.setInt(3, movie.getReleaseYear());
            statement.setLong(4, movie.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Error updating movie", e);
        }
    }

    @Override
    public void delete(Long id) {
        try (PreparedStatement statement = connection.prepareStatement(
                "DELETE FROM movies WHERE id = ?"
        )) {
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Error deleting movie", e);
        }
    }

    private Movie mapRowToMovie(ResultSet resultSet) throws SQLException {
        return Movie.builder()
                .id(resultSet.getLong("id"))
                .title(resultSet.getString("title"))
                .description(resultSet.getString("description"))
                .releaseYear(resultSet.getInt("release_year"))
                .build();
    }
}
