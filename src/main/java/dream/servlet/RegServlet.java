package dream.servlet;

import dream.model.User;
import dream.store.PsqlStoreUser;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RegServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("reg.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("name");
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        User user = new User(
                name,
                email,
                password
        );
        if (user.getName().isEmpty()) {
            req.setAttribute("Ошибка", "Такой пользователь уже существует");
            req.getRequestDispatcher("reg.jsp").forward(req, resp);
        } else {
            PsqlStoreUser.instOf().save(user);
        }
        resp.sendRedirect(req.getContextPath() + "/login.jsp");
    }
}
