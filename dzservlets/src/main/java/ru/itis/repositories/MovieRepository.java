package ru.itis.repositories;

import ru.itis.models.Movie;
import java.util.List;

public interface MovieRepository extends CrudRepository<Movie> {
    List<Long> findPlaylistsByMovieId(Long movieId);
    void addMovieToPlaylist(Long movieId, Long playlistId);
    void removeMovieFromPlaylist(Long movieId, Long playlistId);
    double getAverageRating(Long movieId);
    void addOrUpdateRating(Long userId, Long movieId, int rating);
    List<String> getCommentsForMovie(Long movieId);
    void addComment(Long userId, Long movieId, String comment);
    void removeComment(Long commentId);
}
