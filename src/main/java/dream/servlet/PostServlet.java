package dream.servlet;

import dream.model.Post;
import dream.store.PsqlStorePost;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;

public class PostServlet extends HttpServlet {


    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String edit = req.getParameter("edit");
        String path = edit != null ? "/post/edit.jsp" : "/posts.jsp";
        req.setAttribute("user", req.getSession().getAttribute("user"));
        if (edit == null) {
            req.setAttribute("posts", PsqlStorePost.instOf().findAll());
        }
        req.getRequestDispatcher(path).forward(req, resp);
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        PsqlStorePost.instOf().save(
                new Post(
                        Integer.valueOf(req.getParameter("id")),
                        req.getParameter("name"),
                        "",
                        new Timestamp(System.currentTimeMillis())
                )
        );
        resp.sendRedirect(req.getContextPath() + "/posts.do");
    }
}
