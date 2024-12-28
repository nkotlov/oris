package ru.itis.servlets;

import ru.itis.services.UserService;
import ru.itis.services.UserServiceImpl;
import ru.itis.utils.DataSourceUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/signUp")
public class SignUpServlet extends HttpServlet {
    private UserService userService;

    @Override
    public void init() throws ServletException {
        this.userService = new UserServiceImpl(DataSourceUtil.createDataSource());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/jsp/signUp.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String email = req.getParameter("email");
        String password = req.getParameter("password");

        try {
            if (userService.isEmailExists(email)) {
                req.setAttribute("error", "Этот email уже зарегистрирован.");
                req.getRequestDispatcher("/jsp/signUp.jsp").forward(req, resp);
            } else {
                userService.signUp(username, email, password);
                resp.sendRedirect("/signIn?success=true");
            }
        } catch (Exception e) {
            req.setAttribute("error", "Ошибка при регистрации. Попробуйте снова.");
            req.getRequestDispatcher("/jsp/signUp.jsp").forward(req, resp);
        }
    }
}
