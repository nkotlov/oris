package ru.itis.servlets;

import ru.itis.models.MoodCategory;
import ru.itis.repositories.MoodCategoriesRepository;
import ru.itis.repositories.impl.MoodCategoriesRepositoryJdbcImpl;
import ru.itis.utils.DatabaseConnection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.util.List;

@WebServlet("/mood-categories")
public class MoodCategoriesServlet extends HttpServlet {
    private MoodCategoriesRepository moodCategoriesRepository;

    @Override
    public void init() throws ServletException {
        try {
            Connection connection = DatabaseConnection.getConnection();
            moodCategoriesRepository = new MoodCategoriesRepositoryJdbcImpl(connection);
        } catch (Exception e) {
            throw new ServletException("Failed to initialize database connection", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String searchName = req.getParameter("search");
        List<MoodCategory> categories;

        if (searchName != null && !searchName.isEmpty()) {
            categories = moodCategoriesRepository.findByName(searchName);
        } else {
            categories = moodCategoriesRepository.findAll();
        }

        req.setAttribute("categories", categories);
        req.getRequestDispatcher("/jsp/mood-categories.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");

        if ("create".equals(action)) {
            String name = req.getParameter("name");

            MoodCategory moodCategory = MoodCategory.builder()
                    .name(name)
                    .build();

            moodCategoriesRepository.save(moodCategory);
        } else if ("update".equals(action)) {
            Long id = Long.parseLong(req.getParameter("id"));
            String name = req.getParameter("name");

            MoodCategory moodCategory = MoodCategory.builder()
                    .id(id)
                    .name(name)
                    .build();

            moodCategoriesRepository.update(moodCategory);
        } else if ("delete".equals(action)) {
            Long id = Long.parseLong(req.getParameter("id"));
            moodCategoriesRepository.delete(id);
        }

        resp.sendRedirect("/mood-categories");
    }
}
