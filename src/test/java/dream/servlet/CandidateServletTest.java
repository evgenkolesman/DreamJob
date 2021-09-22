package dream.servlet;


import dream.model.Candidate;
import dream.store.MemStore;
import dream.store.PsqlStoreCandidate;
import dream.store.Store;

import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RunWith(PowerMockRunner.class)
@PrepareForTest(PsqlStoreCandidate.class)
public class CandidateServletTest {

    @Test
    public void whenCreatePost() throws IOException, ServletException {
        Store store = MemStore.instOf();

        PowerMockito.mockStatic(PsqlStoreCandidate.class);
        PowerMockito.when(PsqlStoreCandidate.instOf()).thenReturn(store);

        HttpServletRequest req = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse resp = Mockito.mock(HttpServletResponse.class);

        PowerMockito.when(req.getParameter("id")).thenReturn("0");
        PowerMockito.when(req.getParameter("name")).thenReturn("n");
        PowerMockito.when(req.getParameter("city_id")).thenReturn("c");


        new CandidateServlet().doPost(req, resp);

        Candidate result = (Candidate) store.findAll().iterator().next();
        Assert.assertThat(result.getName(), Is.is("n"));
    }
}