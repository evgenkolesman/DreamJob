package dream.servlet;

import dream.model.Post;
import dream.store.Store;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;

public class PostServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        Store.instOf().save(new Post(0, req.getParameter("name"), req.getPathInfo(), new Timestamp(System.currentTimeMillis())));
        resp.sendRedirect(req.getContextPath() + "/posts.jsp");
    }
}
