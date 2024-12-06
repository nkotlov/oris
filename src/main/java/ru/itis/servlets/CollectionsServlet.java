package ru.itis.servlets;

import ru.itis.models.Collection;
import ru.itis.repositories.CollectionsRepository;
import ru.itis.repositories.impl.CollectionsRepositoryJdbcImpl;
import ru.itis.utils.DatabaseConnection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.util.List;

@WebServlet("/collections")
public class CollectionsServlet extends HttpServlet {
    private CollectionsRepository collectionsRepository;

    @Override
    public void init() throws ServletException {
        try {
            Connection connection = DatabaseConnection.getConnection();
            collectionsRepository = new CollectionsRepositoryJdbcImpl(connection);
        } catch (Exception e) {
            throw new ServletException("Failed to initialize database connection", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String searchName = req.getParameter("search");
        List<Collection> collections;

        if (searchName != null && !searchName.isEmpty()) {
            collections = collectionsRepository.findByName(searchName);
        } else {
            collections = collectionsRepository.findAll();
        }

        req.setAttribute("collections", collections);
        req.getRequestDispatcher("/jsp/collections.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");

        if ("create".equals(action)) {
            String name = req.getParameter("name");
            String description = req.getParameter("description");

            Collection collection = Collection.builder()
                    .name(name)
                    .description(description)
                    .build();

            collectionsRepository.save(collection);
        } else if ("update".equals(action)) {
            Long id = Long.parseLong(req.getParameter("id"));
            String name = req.getParameter("name");
            String description = req.getParameter("description");

            Collection collection = Collection.builder()
                    .id(id)
                    .name(name)
                    .description(description)
                    .build();

            collectionsRepository.update(collection);
        } else if ("delete".equals(action)) {
            Long id = Long.parseLong(req.getParameter("id"));
            collectionsRepository.delete(id);
        }

        resp.sendRedirect("/collections");
    }
}
