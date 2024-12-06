package ru.itis.servlets;

import ru.itis.models.User;
import ru.itis.repositories.UsersRepository;
import ru.itis.repositories.impl.UsersRepositoryJdbcImpl;
import ru.itis.utils.DatabaseConnection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.util.List;

@WebServlet("/profile")
public class UsersServlet extends HttpServlet {
    private UsersRepository usersRepository;

    @Override
    public void init() throws ServletException {
        try {
            Connection connection = DatabaseConnection.getConnection();
            usersRepository = new UsersRepositoryJdbcImpl(connection);
        } catch (Exception e) {
            throw new ServletException("Failed to initialize database connection", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<User> users = usersRepository.findAll();
        req.setAttribute("usersForJsp", users);
        req.getRequestDispatcher("/jsp/users.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String firstName = req.getParameter("first_name");
        String lastName = req.getParameter("last_name");
        String email = req.getParameter("email");
        String password = req.getParameter("password");

        User newUser = User.builder()
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .password(password)
                .build();

        usersRepository.save(newUser);
        resp.sendRedirect("/profile");
    }
}
