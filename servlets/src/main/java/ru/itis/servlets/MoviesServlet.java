package ru.itis.servlets;

import ru.itis.models.Movie;
import ru.itis.repositories.MoviesRepository;
import ru.itis.repositories.impl.MoviesRepositoryJdbcImpl;
import ru.itis.utils.DatabaseConnection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.util.List;

@WebServlet("/movies")
public class MoviesServlet extends HttpServlet {
    private MoviesRepository moviesRepository;

    @Override
    public void init() throws ServletException {
        try {
            Connection connection = DatabaseConnection.getConnection();
            moviesRepository = new MoviesRepositoryJdbcImpl(connection);
        } catch (Exception e) {
            throw new ServletException("Failed to initialize database connection", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String collectionId = req.getParameter("collectionId");
        String moodCategoryId = req.getParameter("moodCategoryId");
        List<Movie> movies;

        if (collectionId != null) {
            movies = moviesRepository.findByCollectionId(Long.parseLong(collectionId));
        } else if (moodCategoryId != null) {
            movies = moviesRepository.findByMoodCategoryId(Long.parseLong(moodCategoryId));
        } else {
            movies = moviesRepository.findAll();
        }

        req.setAttribute("movies", movies);
        req.getRequestDispatcher("/jsp/movies.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");

        if ("create".equals(action)) {
            String title = req.getParameter("title");
            String description = req.getParameter("description");
            int releaseYear = Integer.parseInt(req.getParameter("releaseYear"));

            Movie movie = Movie.builder()
                    .title(title)
                    .description(description)
                    .releaseYear(releaseYear)
                    .build();

            moviesRepository.save(movie);
        } else if ("update".equals(action)) {
            Long id = Long.parseLong(req.getParameter("id"));
            String title = req.getParameter("title");
            String description = req.getParameter("description");
            int releaseYear = Integer.parseInt(req.getParameter("releaseYear"));

            Movie movie = Movie.builder()
                    .id(id)
                    .title(title)
                    .description(description)
                    .releaseYear(releaseYear)
                    .build();

            moviesRepository.update(movie);
        } else if ("delete".equals(action)) {
            Long id = Long.parseLong(req.getParameter("id"));
            moviesRepository.delete(id);
        }

        resp.sendRedirect("/movies");
    }
}
