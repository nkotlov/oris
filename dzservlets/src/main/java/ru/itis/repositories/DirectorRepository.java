package ru.itis.repositories;

import ru.itis.models.Director;
import ru.itis.models.Movie;
import java.util.List;

public interface DirectorRepository extends CrudRepository<Director> {
    List<Movie> findMoviesByDirectorId(Long directorId);
}
