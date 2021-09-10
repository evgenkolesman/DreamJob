package dream.servlet;

import org.apache.log4j.Logger;
import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import dream.model.Post;
import dream.store.Store;
import dream.store.PsqlStorePost;
import dream.store.MemStore;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RunWith(PowerMockRunner.class)
@PrepareForTest(PsqlStorePost.class)
public class PostServletTest{
    Logger logger = Logger.getLogger(PostServletTest.class);

    @Test
    public void whenCreatePost() throws IOException {
        Store store = MemStore.instOf();

        PowerMockito.mockStatic(PsqlStorePost.class);
        PowerMockito.when(PsqlStorePost.instOf()).thenReturn(store);

        HttpServletRequest req = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse resp = Mockito.mock(HttpServletResponse.class);

        PowerMockito.when(req.getParameter("id")).thenReturn("0");
        PowerMockito.when(req.getParameter("name")).thenReturn("n");
        PowerMockito.when(req.getParameter("description")).thenReturn("d");

        try {
            new PostServlet().doPost(req, resp);
        } catch (ServletException e) {
            logger.error("Exception: ", e);
        }

        Post result = (Post) store.findAll().iterator().next();
        Assert.assertThat(result.getName(), Is.is("n"));
        Assert.assertThat(result.getDescription(), Is.is("d"));
    }
}