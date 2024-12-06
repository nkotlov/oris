package ru.itis.repositories;

import ru.itis.models.Movie;

import java.util.List;

public interface MoviesRepository {
    List<Movie> findByCollectionId(Long collectionId);
    List<Movie> findByMoodCategoryId(Long moodCategoryId);
    List<Movie> findAll();
    void save(Movie movie);
    void update(Movie movie);
    void delete(Long id);
}
