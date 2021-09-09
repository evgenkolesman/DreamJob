package dream.servlet;

import dream.model.Candidate;
import dream.store.PsqlStoreCandidate;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CandidateServlet extends HttpServlet {


    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String edit = req.getParameter("edit");
        String path = edit != null ? "/candidate/edit.jsp" : "candidates.jsp";
        req.setAttribute("user", req.getSession().getAttribute("user"));
        if (edit == null) {
            req.setAttribute("candidates", PsqlStoreCandidate.instOf().findAll());
        }
        req.getRequestDispatcher(path).forward(req, resp);
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        PsqlStoreCandidate.instOf().save(new Candidate(0, req.getParameter("name"), req.getParameter("city_id")));
        resp.sendRedirect(req.getContextPath() + "/candidates.do");
    }
}
