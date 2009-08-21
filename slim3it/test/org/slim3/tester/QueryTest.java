package org.slim3.tester;

import slim3.it.meta.BlogMeta;
import slim3.it.model.Blog;

public class QueryTest extends JDOTestCase {

    public void testTypeUnsafe() throws Exception {
        String jdoql =
            "select from " + Blog.class.getName() + " order by key asc";
        pm.newQuery(jdoql).execute();
    }

    public void testTypeSafe() throws Exception {
        BlogMeta b = new BlogMeta();
        from(b).orderBy(b.key.asc()).getResultList();
    }
}