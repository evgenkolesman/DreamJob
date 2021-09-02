package dream.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Objects;

public class DownloadServlet extends HttpServlet {

        @Override
        protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            String name = req.getParameter("1");
//            String name = "AAA.jpg";
            File downloadFile = new File("./");
            for (File file : Objects.requireNonNull(new File("./").listFiles())) {
                if (name.equals(file.getName())) {
                    downloadFile = file;
                    break;
                }
            }
            resp.setContentType("application/octet-stream");
            resp.setHeader("Content-Disposition", "attachment; filename=\"" + downloadFile.getName() + "\"");
            try (FileInputStream stream = new FileInputStream(downloadFile)){
                resp.getOutputStream().write(stream.readAllBytes());
            }
        }
    }


