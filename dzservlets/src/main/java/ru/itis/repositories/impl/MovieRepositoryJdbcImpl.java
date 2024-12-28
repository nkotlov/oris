package ru.itis.repositories.impl;

import ru.itis.models.Movie;
import ru.itis.repositories.MovieRepository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MovieRepositoryJdbcImpl implements MovieRepository {
    private final DataSource dataSource;

    public MovieRepositoryJdbcImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void save(Movie entity) {
        String sql = "INSERT INTO movies (title, description, release_year, director_id, file_id) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, entity.getTitle());
            statement.setString(2, entity.getDescription());
            statement.setInt(3, entity.getReleaseYear());
            statement.setObject(4, entity.getDirectorId());
            statement.setObject(5, entity.getFileId());
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
    public List<Long> findPlaylistsByMovieId(Long movieId) {
        String sql = "SELECT playlist_id FROM movie_playlists WHERE movie_id = ?";
        List<Long> playlistIds = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, movieId);
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
    public void addMovieToPlaylist(Long movieId, Long playlistId) {
        String sql = "INSERT INTO movie_playlists (movie_id, playlist_id) VALUES (?, ?)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, movieId);
            statement.setLong(2, playlistId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void removeMovieFromPlaylist(Long movieId, Long playlistId) {
        String sql = "DELETE FROM movie_playlists WHERE movie_id = ? AND playlist_id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, movieId);
            statement.setLong(2, playlistId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public double getAverageRating(Long movieId) {
        String sql = "SELECT AVG(rating) AS average_rating FROM movie_ratings WHERE movie_id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, movieId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getDouble("average_rating");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }

    @Override
    public void addOrUpdateRating(Long userId, Long movieId, int rating) {
        String sql = "INSERT INTO movie_ratings (user_id, movie_id, rating) VALUES (?, ?, ?) " +
                "ON CONFLICT (user_id, movie_id) DO UPDATE SET rating = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, userId);
            statement.setLong(2, movieId);
            statement.setInt(3, rating);
            statement.setInt(4, rating);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<String> getCommentsForMovie(Long movieId) {
        String sql = "SELECT comment FROM movie_comments WHERE movie_id = ?";
        List<String> comments = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, movieId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                comments.add(resultSet.getString("comment"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return comments;
    }

    @Override
    public void addComment(Long userId, Long movieId, String comment) {
        String sql = "INSERT INTO movie_comments (user_id, movie_id, comment) VALUES (?, ?, ?)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, userId);
            statement.setLong(2, movieId);
            statement.setString(3, comment);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void removeComment(Long commentId) {
        String sql = "DELETE FROM movie_comments WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, commentId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Movie> findById(Long id) {
        String sql = "SELECT * FROM movies WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(mapRowToMovie(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    @Override
    public List<Movie> findAll() {
        String sql = "SELECT * FROM movies";
        List<Movie> movies = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                movies.add(mapRowToMovie(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return movies;
    }

    @Override
    public void update(Movie entity) {
        String sql = "UPDATE movies SET title = ?, description = ?, release_year = ?, director_id = ?, file_id = ? WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, entity.getTitle());
            statement.setString(2, entity.getDescription());
            statement.setInt(3, entity.getReleaseYear());
            statement.setObject(4, entity.getDirectorId());
            statement.setObject(5, entity.getFileId());
            statement.setLong(6, entity.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM movies WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Movie mapRowToMovie(ResultSet resultSet) throws SQLException {
        return Movie.builder()
                .id(resultSet.getLong("id"))
                .title(resultSet.getString("title"))
                .description(resultSet.getString("description"))
                .releaseYear(resultSet.getInt("release_year"))
                .directorId(resultSet.getLong("director_id"))
                .fileId(resultSet.getLong("file_id"))
                .build();
    }
}
