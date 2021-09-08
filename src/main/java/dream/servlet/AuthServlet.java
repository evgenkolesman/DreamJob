package dream.servlet;

import dream.model.User;
import dream.store.PsqlStoreUser;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class AuthServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("error", null);
        req.getRequestDispatcher("/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession sc = req.getSession();
        String email = req.getParameter("email");

        User user = PsqlStoreUser.instOf().findByEmail(email);
        if (user == null) {
            req.setAttribute("error", "Не верный email");
            req.getRequestDispatcher("/login.jsp").forward(req, resp);
        } else {
            sc.setAttribute("user", user);
            resp.sendRedirect(req.getContextPath() + "/posts.do");
        }
    }
}