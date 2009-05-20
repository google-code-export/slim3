package slim3.it.controller.blog;

import org.slim3.tester.JDOControllerTestCase;

import slim3.it.model.BlogMeta;

public class InsertControllerTest extends JDOControllerTestCase {

    public void testRun() throws Exception {
        param("title", "aaa");
        param("content", "bbb");
        start("/blog/insert");
        InsertController controller = getController();
        assertNotNull(controller);
        assertTrue(isRedirect());
        assertEquals("/blog/", getNextPath());
        assertEquals("aaa", requestScope("title"));
        assertEquals("bbb", requestScope("content"));
        BlogMeta m = new BlogMeta();
        assertEquals(1, from(m).getResultList().size());
    }
}
