package ru.itis.servlets;

import ru.itis.services.UserService;
import ru.itis.services.UserServiceImpl;
import ru.itis.utils.DataSourceUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/signIn")
public class SignInServlet extends HttpServlet {
    private UserService userService;

    @Override
    public void init() throws ServletException {
        this.userService = new UserServiceImpl(DataSourceUtil.createDataSource());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String identifier = req.getParameter("identifier");
        String password = req.getParameter("password");

        try {
            String username;
            String role;

            if (identifier.contains("@")) {
                role = userService.signInByEmail(identifier, password);
                username = userService.getUsernameByEmail(identifier);
            } else {
                role = userService.signInByUsername(identifier, password);
                username = identifier;
            }

            HttpSession session = req.getSession();
            session.setAttribute("username", username);
            session.setAttribute("role", role);

            if ("ADMIN".equals(role)) {
                resp.sendRedirect("/admin");
            } else {
                resp.sendRedirect("/catalog");
            }

        } catch (IllegalArgumentException e) {
            req.setAttribute("error", "Неправильный логин или пароль");
            req.getRequestDispatcher("/jsp/signIn.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/jsp/signIn.jsp").forward(req, resp);
    }
}
