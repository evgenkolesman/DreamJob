package dream.servlet;

import dream.model.User;
import dream.store.PsqlStoreUser;
import dream.store.Store;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class RegServlet extends HttpServlet {

    private Logger LOGGER = Logger.getLogger(RegServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String exit = req.getParameter("exit");
        if (exit != null) {
            req.setAttribute("user", null);
            resp.sendRedirect(req.getContextPath());
        }
        req.setAttribute("error", null);
        req.getRequestDispatcher("/reg.jsp").forward(req, resp);
    }

    //    @Override
//    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        String name = req.getParameter("name");
//        String email = req.getParameter("email");
//        String password = req.getParameter("password");
//        User user = new User(
//                name,
//                email,
//                password
//        );
//
//        if (PsqlStoreUser.instOf().findByEmail(email) != null) {
//            req.setAttribute("Ошибка", "Такой пользователь уже существует");
//            req.getRequestDispatcher("reg.jsp").forward(req, resp);
//        } else {
//            PsqlStoreUser.instOf().save(user);
//        }
//        resp.sendRedirect(req.getContextPath() + "/login.jsp");
//    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        boolean rsl = false;
        String email = req.getParameter("email");
        try {
            PsqlStoreUser.instOf().save(
                    new User(
                            req.getParameter("name"),
                            email,
                            req.getParameter("password")
                    )
            );
            rsl = true;
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
        } catch (Exception e) {
            LOGGER.error("ERROR: ", e);
        }
        if (!rsl) {
            req.setAttribute(
                    "error",
                    String.format(
                            "Пользователь с email: %s уже зарегистрирован.",
                            email
                    )
            );
            req.getRequestDispatcher("reg.jsp").forward(req, resp);
        }
    }
}
