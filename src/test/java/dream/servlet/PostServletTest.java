package dream.servlet;

import dream.model.Model;
import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
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

import static org.mockito.Mockito.mock;

@RunWith(PowerMockRunner.class)
@PrepareForTest(PsqlStorePost.class)
public class PostServletTest {

    @Test
    public void whenCreatePost() throws IOException, ServletException {
        Store store = MemStore.instOf();

        PowerMockito.mockStatic(PsqlStorePost.class);
        PowerMockito.when(PsqlStorePost.instOf()).thenReturn(store);

        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);

        PowerMockito.when(req.getParameter("id")).thenReturn("0");
        PowerMockito.when(req.getParameter("name")).thenReturn("n");
        PowerMockito.when(req.getParameter("description")).thenReturn("d");

        new PostServlet().doPost(req, resp);

        Post result = (Post) store.findAll().iterator().next();
        Assert.assertThat(result.getName(), Is.is("n"));
        Assert.assertThat(result.getDescription(), Is.is("d"));
    }

}